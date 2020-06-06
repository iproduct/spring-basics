package demos.spring.vehicles.web;

import demos.spring.vehicles.model.Brand;
import demos.spring.vehicles.model.EngineType;
import demos.spring.vehicles.model.Offer;
import demos.spring.vehicles.service.BrandService;
import demos.spring.vehicles.service.OfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
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

    @GetMapping
    public String getBrands(Model brands) {
        brands.addAttribute("offers", offerService.getOffers()); // set model data
        return "offers";
    }

    @GetMapping("/add")
    public String getOfferForm(Model model) {
        model.addAttribute("offer", new Offer());
        model.addAttribute("brands", brandService.getBrands());
        return "offer-add";
    }

    @PostMapping("/add")
    public String createNewOffer(@ModelAttribute("offer") Offer offer, Errors errors) {
        if(errors.hasErrors()) {
            log.error("Error creating offer: {}", errors.getAllErrors());
            return "offer-add";
        }
        try {
//            Brand brand = brandService.getBrandById(1L);
//            offer.setModel(brand.getModels().get(0));
            offerService.createOffer(offer);
        } catch(Exception ex) {
            log.error("Error creating offer", ex);
            return "offer-add";
        }

        return "redirect:/offers";
    }


}
