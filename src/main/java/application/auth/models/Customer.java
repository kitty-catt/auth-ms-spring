package application.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class Customer {

    @ApiModelProperty(notes = "The database generated customer ID")
    @Getter
    @Setter
    private String _id;

    @ApiModelProperty(notes = "Revision represents an opaque hash value over the contents of a document.")
    @SuppressWarnings("unused")
    private String _rev;

    @ApiModelProperty(notes = "The customer username")
    private String username;

    @ApiModelProperty(notes = "The customer password")
    private String password;

    @ApiModelProperty(notes = "The customer first name")
    private String firstName;

    @ApiModelProperty(notes = "The customer last name")
    private String lastName;

    @ApiModelProperty(notes = "The customer email id")
    private String email;

    @ApiModelProperty(notes = "The customer image url")
    private String imageUrl;

}
