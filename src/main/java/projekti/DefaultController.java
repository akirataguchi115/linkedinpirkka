package projekti;

import java.sql.Timestamp;
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
    private PostRepository postRepository;
    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index(Model model) {
        //check login
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (currentUser.equals("anonymousUser")) {
            model.addAttribute("isloggedin", false);
            return "index";
        }
        model.addAttribute("isloggedin", true);

        List<Post> shownPosts = new ArrayList<>();
        Account loggedInUser = accountRepository.findByEmail(currentUser);
        for (Connection connection : connectionRepository.findByToAndAcceptedOrFromAndAccepted(loggedInUser, true, loggedInUser, true)) {
            Account accountWithPosts = connection.getFrom();
            if (connection.getFrom().getName().equals(loggedInUser.getName())) {
                accountWithPosts = connection.getTo();
            }
            for (Post post : postRepository.findByUrl(accountWithPosts.getUrl())) {
                shownPosts.add(post);
            }
        }
        for (Post post : postRepository.findByUrl(loggedInUser.getUrl())) {
            shownPosts.add(post);
        }
        model.addAttribute("shownposts", shownPosts);
        model.addAttribute("loggedinuser", loggedInUser);

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
        accountRepository.save(new Account(email, passwordEncoder.encode(password), name, url, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, new ArrayList<>(), null));
        return "redirect:/login";
    }

    @PostMapping("/posts/{user}")
    public String createPost(@PathVariable String user, @RequestParam String content) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Post post = new Post(content, now, new ArrayList<>(), user, new ArrayList<>());

        postRepository.save(post);
        return "redirect:/";
    }
}
