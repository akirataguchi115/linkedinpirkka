package projekti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
        accountRepository.save(new Account(email, passwordEncoder.encode(password), name, url, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null));
        return "redirect:/login";
    }

    @GetMapping("/users/{url}")
    public String showProfile(Model model, @PathVariable String url) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Account shownProfile = accountRepository.findByUrl(url);
        if (currentUser.equals("anonymousUser")) {
            model.addAttribute("connection", "null");
        } else {
            Account loggedInUser = accountRepository.findByEmail(currentUser);
            model.addAttribute("loggedin", loggedInUser);
            model.addAttribute("name", accountRepository.findByUrl(url).getName());
            model.addAttribute("account", accountRepository.findByUrl(url));
            Connection connection = connectionRepository.findByFromAndTo(accountRepository.findByEmail(currentUser), accountRepository.findByUrl(url));
            if (connection == null) {
                connection = connectionRepository.findByFromAndTo(accountRepository.findByUrl(url), accountRepository.findByEmail(currentUser));
            }
            if (loggedInUser.getUrl().equals(url)) {
                model.addAttribute("connection", "itsyou");
                List<Connection> list = connectionRepository.findByToOrAccepted(loggedInUser, true);
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
        if (shownProfile.getPicture() != null) {
            System.out.println(shownProfile.getPicture().getId());
            model.addAttribute("profilepicture", shownProfile.getPicture().getId());
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

    @Transactional
    @PostMapping("/connection/{user}")
    public String changeConnection(Model model, @PathVariable String user, @RequestParam boolean connect) {
        Account currentUser = accountRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Account anotherUser = accountRepository.findByUrl(user);
        Connection connection = connectionRepository.findByFromAndTo(currentUser, anotherUser);
        if (connection == null) {
            connection = connectionRepository.findByFromAndTo(anotherUser, currentUser);
        }
        if (connect) {
            connection.setAccepted(connect);
            connectionRepository.save(connection);
        } else {
            connectionRepository.deleteByFromAndTo(anotherUser, currentUser);
        }
        return "redirect:/users/" + currentUser.getUrl();
    }
    
    @Transactional
    @GetMapping("/pictures/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        Picture picture = pictureRepository.getOne(id);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(picture.getMediaType()));
        headers.setContentLength(picture.getSize());
        headers.add("Contnet-Disposition", "attachment; filename=" + picture.getName());

        return new ResponseEntity<>(picture.getContent(), headers, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/pictures")
    public String setPicture(@RequestParam("file") MultipartFile file) throws IOException {
        Account currentUser = accountRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        Picture picture = new Picture();
        picture.setName(file.getOriginalFilename());
        picture.setMediaType(file.getContentType());
        picture.setSize(file.getSize());
        picture.setContent(file.getBytes());
        currentUser.setPicture(picture);
        pictureRepository.save(picture);

        return "redirect:/users/" + currentUser.getUrl();
    }
}
