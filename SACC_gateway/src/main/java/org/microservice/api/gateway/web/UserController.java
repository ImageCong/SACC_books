package org.microservice.api.gateway.web;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.microservice.pub.commons.entity.Book;
import org.microservice.pub.commons.entity.RecommendBook;
import org.microservice.pub.commons.entity.User;
import org.microservice.pub.commons.service.BookService;
import org.microservice.pub.commons.service.ReaderService;
import org.microservice.pub.commons.service.RecommendBookService;
import org.microservice.pub.commons.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    @Reference(version = "1.0.0")
    private UserService userService;
    @Reference(version = "1.0.0")
    private BookService bookService;
    @Reference(version = "1.0.0")
    private RecommendBookService recommendBookService;
    @Reference(version = "1.0.0")
    private ReaderService readerService;

    @Autowired
    private Gson gsonObject;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpSession session, HttpServletResponse response) throws IOException {

        User loginUser = userService.loginCheck(username, password);

        if (loginUser != null) {
            log.info("================login success,show index==============");
            List<Book> books = bookService.listBook();
            session.setAttribute("user", loginUser);
            return gsonObject.toJson(books);
        } else {
            response.sendRedirect("/");
            // 这里的return不重要，之前已经被重定向了
            return "redirect";
        }
    }

    @GetMapping("/recommend")
    public String toRecommend(HttpSession session, HttpServletResponse response) throws IOException {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            log.warn("未授权用户进入....");
            response.sendRedirect("/");
            return "redirect";
        }
        List<RecommendBook> recommendBooks = recommendBookService.selectRecommend(user.getUserCode());
        return gsonObject.toJson(recommendBooks);
    }


    @GetMapping("/like")
    public String clickLikeButton(@RequestParam Integer bookCode,HttpSession session){
        User user = (User) session.getAttribute("user");

        int isAddSuccess = readerService.addViewTimes(user.getUserCode(), bookCode);

        if (isAddSuccess == 1){
            log.info("---------------add view time success---------------");
            session.setAttribute("isAddSuccess", "true");
        }else {
            log.info("---------------add view time fail---------------");
            session.setAttribute("isAddSuccess", "fail");
        }

        return gsonObject.toJson(isAddSuccess);
    }


}
