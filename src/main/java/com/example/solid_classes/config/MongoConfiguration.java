package com.example.solid_classes.config;

import java.math.BigDecimal;
import java.util.List;

import org.bson.types.Decimal128;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(
    basePackages = "com.example.solid_classes.core",
    includeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
        type = org.springframework.context.annotation.FilterType.REGEX,
        pattern = ".*repository\\.mongo.*"
    )
)
public class MongoConfiguration {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        List<Converter<?, ?>> converters = List.of(
                new BigDecimalToDecimal128Converter(),
                new Decimal128ToBigDecimalConverter()
        );
        return new MongoCustomConversions(converters);
    }

    @WritingConverter
    public static class BigDecimalToDecimal128Converter implements Converter<BigDecimal, Decimal128> {
        @Override
        public Decimal128 convert(BigDecimal source) {
            return source == null ? null : new Decimal128(source);
        }
    }

    @ReadingConverter
    public static class Decimal128ToBigDecimalConverter implements Converter<Decimal128, BigDecimal> {
        @Override
        public BigDecimal convert(Decimal128 source) {
            return source == null ? null : source.bigDecimalValue();
        }
    }
}
