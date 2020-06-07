package demos.spring.vehicles.web;

import demos.spring.vehicles.model.Brand;
import demos.spring.vehicles.model.EngineType;
import demos.spring.vehicles.model.Offer;
import demos.spring.vehicles.service.BrandService;
import demos.spring.vehicles.service.OfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/offers")
@Slf4j
public class OfferController {
    private BrandService brandService;
    private OfferService offerService;

    @Autowired
    public OfferController(BrandService brandService, OfferService offerService) {
        this.brandService = brandService;
        this.offerService = offerService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));

    }

    @GetMapping
    public String getBrands(Model brands) {
        brands.addAttribute("offers", offerService.getOffers()); // set model data
        return "offers";
    }

    @GetMapping("/add")
    public String getOfferForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if(session.getAttribute("user") == null) {
            redirectAttributes.addAttribute("redirectUrl", "/offers/add");
            return "redirect:/auth/login";
        }
        if (!model.containsAttribute("offer")) {
            model.addAttribute("offer", new Offer());
        }
        model.addAttribute("brands", brandService.getBrands());
        return "offer-add";
    }

    @PostMapping("/add")
    public String createNewOffer(@Valid @ModelAttribute("offer") Offer offer, final BindingResult binding, RedirectAttributes redirectAttributes) {
        if(binding.hasErrors()) {
            log.error("Error creating offer: {}", binding.getAllErrors());
            redirectAttributes.addFlashAttribute("offer", offer);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.offer", binding);
            return "redirect:add";
        }
        try {
            offerService.createOffer(offer);
        } catch(Exception ex) {
            log.error("Error creating offer", ex);
            redirectAttributes.addFlashAttribute("offer", offer);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.offer", binding);
            return "redirect:add";
        }

        return "redirect:/offers";
    }


}
