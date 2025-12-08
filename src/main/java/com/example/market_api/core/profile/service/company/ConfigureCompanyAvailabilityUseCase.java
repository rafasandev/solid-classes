package com.example.market_api.core.profile.service.company;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.profile.dto.company.CompanyAvailabilityForm;
import com.example.market_api.core.profile.dto.company.CompanyDailyAvailabilityForm;
import com.example.market_api.core.profile.dto.company.CompanyProfileResponseDto;
import com.example.market_api.core.profile.dto.company.TimeRangeForm;
import com.example.market_api.core.profile.mapper.ProfileMapper;
import com.example.market_api.core.profile.model.company.CompanyProfile;
import com.example.market_api.core.profile.model.company.value.CompanyDailyAvailability;
import com.example.market_api.core.user.model.User;
import com.example.market_api.core.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfigureCompanyAvailabilityUseCase {

    private final CompanyProfileService companyProfileService;
    private final UserService userService;
    private final ProfileMapper profileMapper;

    @Transactional
    public CompanyProfileResponseDto configureAvailability(UUID companyId, CompanyAvailabilityForm form) {
        CompanyProfile company = companyProfileService.getById(companyId);
        User loggedUser = userService.getLoggedInUser();
        validateCompanyOwnership(company, loggedUser);

        List<Integer> normalizedWeekDays = normalizeWeekDays(form.getWeekDaysAvailable());
        List<CompanyDailyAvailability> availabilitySlots = buildAvailability(form.getDailyAvailability(),
                normalizedWeekDays);

        company.setWeekDaysAvailable(normalizedWeekDays);
        company.setDailyAvailableTimeRanges(availabilitySlots);

        CompanyProfile saved = companyProfileService.save(company);
        return profileMapper.toResponseDto(saved);
    }

    private void validateCompanyOwnership(CompanyProfile company, User loggedUser) {
        if (company == null || loggedUser == null || !company.getId().equals(loggedUser.getId())) {
            throw new BusinessRuleException("Usuário logado não pode alterar a disponibilidade desta empresa");
        }
    }

    // Método auxiliar para normalizar e validar os dias da semana
    private List<Integer> normalizeWeekDays(List<Integer> weekDays) {

        // Se a lista estiver vazia, lança uma exceção
        if (weekDays == null || weekDays.isEmpty()) {
            throw new BusinessRuleException("Informe pelo menos um dia da semana disponível");
        }

        // Remoção de valores duplicados e validação dos dias
        return weekDays.stream()
                .map(day -> {
                    if (day == null || day < 0 || day > 6)
                        throw new BusinessRuleException("Os dias da semana devem estar entre 0 (domingo) e 6 (sábado)");

                    return day;
                })
                .collect(Collectors.collectingAndThen(Collectors.toCollection(LinkedHashSet::new), ArrayList::new));
    }

    // Método auxiliar para construir os horários disponíveis
    private List<CompanyDailyAvailability> buildAvailability(
            List<CompanyDailyAvailabilityForm> dailyAvailabilityForms,
            List<Integer> allowedDays) {

        // Validação básica da lista de formulários
        if (dailyAvailabilityForms == null || dailyAvailabilityForms.isEmpty()) {
            throw new BusinessRuleException("Informe os horários disponíveis para os dias selecionados");
        }

        // Conversão da lista de dias permitidos para um conjunto para facilitar a validação
        Set<Integer> allowedDaySet = new LinkedHashSet<>(allowedDays);

        // Agrupamento dos formulários por dia da semana
        Map<Integer, List<CompanyDailyAvailabilityForm>> groupedForms = dailyAvailabilityForms.stream()
                .collect(Collectors.groupingBy(CompanyDailyAvailabilityForm::getWeekDay));

        // Verificação se todos os dias permitidos possuem formulários associados
        if (!allowedDaySet.equals(groupedForms.keySet())) {
            throw new BusinessRuleException("Todos os dias informados devem possuir ao menos um intervalo configurado");
        }

        // Inicialização da lista de horários disponíveis
        List<CompanyDailyAvailability> slots = new ArrayList<>();

        // Construção e validação dos horários para cada dia
        for (Map.Entry<Integer, List<CompanyDailyAvailabilityForm>> entry : groupedForms.entrySet()) {
            Integer day = entry.getKey();
            if (!allowedDaySet.contains(day)) {
                throw new BusinessRuleException("Dia da semana " + day + " não está na lista de dias habilitados");
            }
            // Construção dos horários para o dia atual
            List<CompanyDailyAvailability> daySlots = entry.getValue().stream()
                    .flatMap(form -> form.getTimeRanges().stream()
                            .map(range -> createSlot(day, range)))
                    .sorted(Comparator.comparing(CompanyDailyAvailability::getStartTime))
                    .toList();

            // Validação dos horários do dia atual
            validateDaySlots(day, daySlots);

            // Adiciona os horários validados à lista principal
            slots.addAll(daySlots);

            // Exemplo da lista final:
            // [CompanyDailyAvailability{weekDay=1, startTime=09:00, endTime=12:00},
            //  CompanyDailyAvailability{weekDay=1, startTime=13:00, endTime=17:00},
            //  CompanyDailyAvailability{weekDay=3, startTime=10:00, endTime=15:00}]
        }

        return slots;
    }

    // Método auxiliar para criar um horário disponível a partir do formulário
    private CompanyDailyAvailability createSlot(Integer day, TimeRangeForm rangeForm) {
        LocalTime startTime = rangeForm.getStartTime();
        LocalTime endTime = rangeForm.getEndTime();

        if (startTime == null || endTime == null) {
            throw new BusinessRuleException("Horários inicial e final devem ser informados para todos os intervalos");
        }

        if (!startTime.isBefore(endTime)) {
            throw new BusinessRuleException("O horário inicial deve ser menor que o horário final em cada intervalo");
        }

        return CompanyDailyAvailability.builder()
                .weekDay(day)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    private void validateDaySlots(Integer day, List<CompanyDailyAvailability> slots) {
        if (slots == null || slots.isEmpty()) {
            throw new BusinessRuleException("Informe ao menos um horário disponível para o dia " + day);
        }

        CompanyDailyAvailability previous = null;
        for (CompanyDailyAvailability current : slots) {
            if (previous != null && !current.getStartTime().isAfter(previous.getEndTime())) {
                throw new BusinessRuleException("Os intervalos do dia " + day + " não podem se sobrepor");
            }
            previous = current;
        }
    }
}
