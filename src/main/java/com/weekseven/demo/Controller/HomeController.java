package com.weekseven.demo.Controller;

import com.weekseven.demo.Model.AppUser;

import com.weekseven.demo.Model.NewsApi;
import com.weekseven.demo.Model.Profile;
import com.weekseven.demo.Repository.AppUserRepository;
import com.weekseven.demo.Repository.ProfileRepository;
import com.weekseven.demo.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;


@Controller
public class HomeController {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    ProfileRepository profileRepository;





    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String newUser(Model model) {
        model.addAttribute("newuser", new AppUser());

        return "registerform";
    }

    @PostMapping("/register")
    public String processUser(@Valid @ModelAttribute("newuser") AppUser newusers, BindingResult result) {
        if (result.hasErrors()) {
            return "registerform";
        }
        newusers.addRole(roleRepository.findByRoleName("USER"));
        appUserRepository.save(newusers);
        return "redirect:/login";
    }

        @GetMapping("/")
        public String showNews(@Valid @ModelAttribute("newsApi") NewsApi newsApis, Model model){

            RestTemplate restTemplate = new RestTemplate();
            newsApis = restTemplate.getForObject("https://newsapi.org/v2/top-headlines?sources=techcrunch&apiKey=d4a451c5d3c74eda940441948d032ab5", NewsApi.class);
           model.addAttribute("article",newsApis.getArticles());

            return "index";
        }

        @GetMapping("/selectcategory")
        public String selectCategory(Model model){
        model.addAttribute("profile",new Profile());
        return "selectcategory";

        }
        @GetMapping("/{category}")
        public String PoliticsNews(@PathVariable("category") String category,@Valid @ModelAttribute("newsApi") NewsApi newsApis, Model model){

            RestTemplate restTemplate = new RestTemplate();
            newsApis = restTemplate.getForObject("https://newsapi.org/v2/top-headlines?"+category+"&apiKey=d4a451c5d3c74eda940441948d032ab5", NewsApi.class);
            model.addAttribute("article",newsApis.getArticles());

            return "userdisplay";
        }

}