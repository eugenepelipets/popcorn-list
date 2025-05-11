package com.eugenepelipets.popcornlist.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MpaDto {
    @NotNull
    private Integer id;
    private String name;
}