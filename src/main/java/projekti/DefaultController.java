package projekti;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DefaultController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/")
    public String search(Model model, @RequestParam String search) {
        List<Account> lista = accountRepository.findByNameContainingIgnoreCase(search);
        model.addAttribute("list", accountRepository.findByNameContainingIgnoreCase(search));
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, String password, String name, String url) {
        accountRepository.save(new Account(email, passwordEncoder.encode(password), name, url, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new Picture()));
        return "redirect:/login";
    }

    @GetMapping("/users/{url}")
    public String showProfile(Model model, @PathVariable String url) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("name", accountRepository.findByUrl(url).getName());
        model.addAttribute("account", accountRepository.findByUrl(url));
        return "profile";
    }
}
