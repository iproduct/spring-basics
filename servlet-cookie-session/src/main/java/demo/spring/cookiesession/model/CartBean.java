package demo.spring.cookiesession.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartBean implements Serializable {
	@NonNull
	private Book book;
	@NonNull
	private int quantity;
}
