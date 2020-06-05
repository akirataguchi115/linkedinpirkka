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
    private ConnectionRepository connectionRepository;
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
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (currentUser.equals("anonymousUser")) {
            model.addAttribute("connection", "null");
        } else {
            Account loggedInUser = accountRepository.findByEmail(currentUser);
            System.out.println("Logged in: " + currentUser);
            model.addAttribute("name", accountRepository.findByUrl(url).getName());
            model.addAttribute("account", accountRepository.findByUrl(url));
            Connection connection = connectionRepository.findByFromAndTo(accountRepository.findByEmail(currentUser), accountRepository.findByUrl(url));
            if (loggedInUser.getUrl().equals(url)) {
                model.addAttribute("connection", "itsyou");
                List<Connection> list = connectionRepository.findByFromOrTo(loggedInUser, loggedInUser);
                if (!list.isEmpty()) {
                    model.addAttribute("connections", list);
                }
            } else if (connection == null) {
                model.addAttribute("connection", "null");
            } else if (!connection.getAccepted()) {
                model.addAttribute("connection", "sent");
            } else if (connection.getAccepted()) {
                model.addAttribute("connection", "confirmed");
            }
        }
        return "profile";
    }

    @PostMapping("/users/{url}")
    public String sendRequest(Model model, @PathVariable String url) {
        Account from = accountRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Account to = accountRepository.findByUrl(url);
        connectionRepository.save(new Connection(from, to, false));
        return "redirect:/users/" + url;
    }
}
