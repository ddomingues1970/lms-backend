package com.danieldomingues.lms.mapper;

import com.danieldomingues.lms.domain.Course;
import com.danieldomingues.lms.web.dto.CourseDto;

public final class CourseMapper {
    private CourseMapper() { }

    public static CourseDto toDTO(Course c) {
        if (c == null) return null;
        return CourseDto.builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .startDate(c.getStartDate())
                .endDate(c.getEndDate())
                .build();
    }

    public static Course toEntity(CourseDto dto) {
        if (dto == null) return null;
        Course c = new Course();
        // id opcional (normalmente null no create)
        c.setId(dto.getId());
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        c.setStartDate(dto.getStartDate());
        c.setEndDate(dto.getEndDate());
        return c;
    }

    /** Atualiza um entity existente com dados do DTO (para UPDATE). */
    public static void copyToEntity(CourseDto dto, Course target) {
        if (dto == null || target == null) return;
        target.setName(dto.getName());
        target.setDescription(dto.getDescription());
        target.setStartDate(dto.getStartDate());
        target.setEndDate(dto.getEndDate());
    }
}
