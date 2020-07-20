package application.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {

	@Getter @Setter private String customerId;
	@Getter @Setter private String username;
	@Getter @Setter private String password;

}
