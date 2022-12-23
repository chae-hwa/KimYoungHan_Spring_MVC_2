package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
//@ScriptAssert(lang = "javascrippt", script = "_this.price * _this.quantity > 10000", message = "10000원 이상 등록해주세요.")
public class Item {

    // groups = ~.class -> ~클래스에만 적용
    @NotNull(groups = UpdateCheck.class)
    private Long id;
    /** 메세지 찾는 순서
     * 1. errors.properties에서 먼저 찾고
     * 2. 없으면 어노테이션의 message 속성 적용
     * 3. 또 없으면 라이브러리 기본 값 사용
     */
    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;
    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max = 1000000)
    private Integer price;
    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999, groups = {UpdateCheck.class})
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
