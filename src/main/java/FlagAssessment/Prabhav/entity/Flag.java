package FlagAssessment.Prabhav.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@lombok
public class Flag {
    @NotBlank
    private String name;

    @NotNull
    private FlagState state;        // ON, OFF, DEFAULT

    private boolean defaultValue;   // used when state == DEFAULT

    // Constructors, getters, setters...
    public Flag() {}

    public Flag(String name, FlagState state, boolean defaultValue) {
        this.name = name;
        this.state = state;
        this.defaultValue = defaultValue;
    }
}