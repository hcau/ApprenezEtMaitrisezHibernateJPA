package com.hibernate4all.tutorial.converter;

import com.hibernate4all.tutorial.domain.Certification;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

// Dès qu'une certification d'un objet du domaine est vu, le converter sera appliqué.
@Converter(autoApply = true)
public class CertificationAttributeConverter implements AttributeConverter<Certification, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Certification attribute) {
        return attribute != null ? attribute.getKey() : null;
    }

    @Override
    public Certification convertToEntityAttribute(Integer dbData) {
        return Stream.of(Certification.values())
                .filter(certif -> certif.getKey().equals(dbData))
                .findFirst().orElse(null);
    }
}
