package com.task_project.campaign_manager.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter @Setter
public class UserDto {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @NotBlank
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be greater than or equal to 0")
    private BigDecimal balance;

    public UserDto(String name) {
        this.username = name;
        this.password = "";
        this.balance = new BigDecimal(0);
    }

    public UserDto(String name, String password) {
        this.username = name;
        this.password = password;
        this.balance = new BigDecimal(0);
    }

}
