package project_skripsi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAddressRequest {

    @NotBlank
    @JsonIgnore
    private String contactId;

    @NotBlank
    @Size(max = 100)
    private String street;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 100)
    private String province;

    @NotBlank
    @Size(max = 100)
    private String country;

    @NotBlank
    @Size(max = 100)
    private String postalCode;

}
