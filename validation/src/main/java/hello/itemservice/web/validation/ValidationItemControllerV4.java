package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }

    /** validatied 검증 순서
     * 1. @ModelAttribute에서 검증 타입이 일치하면 @Validated 메세지 오류 출력(ex ? ~ ?까지 가능합니다. 등)
     * 2. 검증 타입이 불일치하면 @Validated 메세지 오류 대신, typeMismatch FieldError 추가(ex 숫자가 아닙니다. 등)
    * */
//    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
        // 검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            return "validation/v4/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    // SaveCheck.class만 적용하도록
    @PostMapping("/add")
    public String addItemV2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
        // 검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            return "validation/v4/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

//    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {
        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if(bindingResult.hasErrors()){
            return "validation/v4/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable Long itemId, @Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {
        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if(bindingResult.hasErrors()){
            return "validation/v4/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }

}

