package com.store.book.controller;

import com.store.book.Repository.*;
import com.store.book.entity.*;
import com.store.book.entity.bean.CartBean;
import com.store.book.entity.bean.ProductBean;
import com.store.book.service.PagingService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ToyRepository toyRepository;

    @Autowired
    private CategoryToyRespository categoryToyRespository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryBookRespository categoryBookRespository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private CategoryStudyRespository categoryStudyRespository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DetailOrderRepository detailOrderRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @RequestMapping(value="/list", method= RequestMethod.GET)
    public String listProduct(ModelMap model, Principal principal) {
        String userName = principal.getName();
        AppUser user = appUserRepository.findByUserName(userName);
        Client client = clientRepository.findByIdAppUser(user.getUserId());
        model.addAttribute("idClient", client.getIdClient());
        List<Toy> toys = toyRepository.findAll();
        if (toys != null) {
            model.addAttribute("toys", toys);
        }

        List<Book> books = bookRepository.findAll();
        if (books != null) {
            model.addAttribute("books", books);
        }

        List<Study> studies = studyRepository.findAll();
        if (studies != null) {
            model.addAttribute("studies", studies);
        }
        return "productList";
    }

    @PostMapping("/save")
    public String save(String idClient, RedirectAttributes redirect, @RequestParam("idProduct") String idProduct, @RequestParam("number") Optional<Integer> number) {
        Orders order = orderRepository.findByClientIdAndStatus(idClient,"P");
        if (order == null){
            order = new Orders();
            order.setClient(clientRepository.findByIdClient(idClient));
            String prefix = "OD";
            Integer id = orderRepository.getMaxRecord();
            if (id == null) id = 0;
            String generatedId = prefix + StringUtils.leftPad(String.valueOf(id + 1), 4, "0");
            order.setIdOrder(generatedId);
            DetailOrder detailOrder = new DetailOrder();
            detailOrder.setProductId(idProduct);
            Integer amoumnt = number.orElse(1);
            detailOrder.setAmount(amoumnt.longValue());
            detailOrder.setOrders(order);
            List<DetailOrder> list = order.getDetailOrder();
            list.add(detailOrder);
            order.setDetailOrder(list);
            order.setStatus("P");
            orderRepository.save(order);
        }
        else{
            DetailOrder detailOrder = detailOrderRepository.findByAndOrderIdAndProductId(order.getIdOrder(),idProduct);
            if (detailOrder == null) {
                detailOrder = new DetailOrder();
                detailOrder.setProductId(idProduct);
                Integer amoumnt = number.orElse(1);
                detailOrder.setAmount(amoumnt.longValue());
                detailOrder.setOrders(order);
                List<DetailOrder> list = order.getDetailOrder();
                list.add(detailOrder);
                order.setDetailOrder(list);
                orderRepository.save(order);
            }else {
                Integer amoumnt = number.orElse(1);
                detailOrder.setAmount(detailOrder.getAmount() + amoumnt.longValue());
                detailOrderRepository.save(detailOrder);
            }
        }
        redirect.addFlashAttribute("success", "Saved successfully!");
        return "redirect:/order/list";
    }
    @RequestMapping(value="/cart/{idClient}", method= RequestMethod.GET)
    public String getCart(ModelMap model, @PathVariable(value = "idClient") String idClient) {
        model.addAttribute("idClient", idClient);
        Orders order = orderRepository.findByClientIdAndStatus(idClient,"P");
        if (order != null) {
            CartBean cartBean = new CartBean();
            cartBean.setIdOrder(order.getIdOrder());
            List<DetailOrder> list = order.getDetailOrder();
            if (list.size() != 0) {
                List<ProductBean> listProduct = new ArrayList<ProductBean>();
                for (DetailOrder detailOrder : list) {
                    ProductBean productBean = new ProductBean();
                    productBean.setIdProduct(detailOrder.getProductId());
                    productBean.setAmount(detailOrder.getAmount());
                    if (detailOrder.getProductId().startsWith("B")) {
                        Book book = bookRepository.findByIdBook(detailOrder.getProductId());
                        productBean.setNameProduct(book.getNameBook());
                        productBean.setPrice(book.getPrice());
                        productBean.setTotalPrice(book.getPrice() * detailOrder.getAmount());
                    } else if (detailOrder.getProductId().startsWith("T")) {
                        Toy toy = toyRepository.findByIdToy(detailOrder.getProductId());
                        productBean.setNameProduct(toy.getNameToy());
                        productBean.setPrice(toy.getPrice());
                        productBean.setTotalPrice(toy.getPrice() * detailOrder.getAmount());
                    } else {
                        Study study = studyRepository.findByIdStudy(detailOrder.getProductId());
                        productBean.setNameProduct(study.getNameStudy());
                        productBean.setPrice(study.getPrice());
                        productBean.setTotalPrice(study.getPrice() * detailOrder.getAmount());
                    }
                    listProduct.add(productBean);
                }
                long total = 0;
                for (ProductBean productBean : listProduct) {
                    total += productBean.getTotalPrice().longValue();
                }
                cartBean.setProductBean(listProduct);
                cartBean.setTotal(total);
                if (cartBean != null) {
                    model.addAttribute("cartBean", cartBean);
                }
            }else model.addAttribute("alert","Cart empty");
        }else {
            model.addAttribute("alert","Cart empty");
        }
        return "cart";
    }

    @PostMapping("/updateCart")
    public String updateCart(RedirectAttributes redirect, @RequestParam("idProduct") String idProduct, @RequestParam("idOrder") String idOrder, @RequestParam("idClient") String idClient, @RequestParam("number") Optional<Integer> number) {
        DetailOrder detailOrder = detailOrderRepository.findByAndOrderIdAndProductId(idOrder,idProduct);
        Integer amoumnt = number.orElse(1);
        detailOrder.setAmount(amoumnt.longValue());
        detailOrderRepository.save(detailOrder);
        redirect.addFlashAttribute("success", "Update successfully!");
        return "redirect:/order/cart/"+idClient;
    }

    @PostMapping("/buy")
    public String buyOrder(RedirectAttributes redirect, @RequestParam("idOrder") String idOrder, @RequestParam("total") Long total) {
        Orders order = orderRepository.findByIdOrder(idOrder);
        List<DetailOrder> list = order.getDetailOrder();
        for (DetailOrder detailOrder : list) {
            if (detailOrder.getProductId().startsWith("B")) {
                Book book = bookRepository.findByIdBook(detailOrder.getProductId());
                book.setAmount(book.getAmount() - detailOrder.getAmount());
                bookRepository.save(book);
            } else if (detailOrder.getProductId().startsWith("T")) {
                Toy toy = toyRepository.findByIdToy(detailOrder.getProductId());
                toy.setAmount(toy.getAmount() - detailOrder.getAmount());
                toyRepository.save(toy);
            } else {
                Study study = studyRepository.findByIdStudy(detailOrder.getProductId());
                study.setAmount(study.getAmount() - detailOrder.getAmount());
                studyRepository.save(study);
            }
        }
        order.setStatus("D");
        order.setTotal(total);
        order.setDateBuy(new Date());
        orderRepository.save(order);
        redirect.addFlashAttribute("success", "Buy successfully!");
        return "redirect:/home";
    }

    @PostMapping("/delete")
    @Transactional
    public String delete(RedirectAttributes redirect, @RequestParam("idProduct") String idProduct, @RequestParam("idOrder") String idOrder, @RequestParam("idClient") String idClient) {
        DetailOrder detailOrder = detailOrderRepository.findByAndOrderIdAndProductId(idOrder,idProduct);
        detailOrderRepository.delete(detailOrder);
        redirect.addFlashAttribute("success", "Removed successfully!");
        return "redirect:/order/cart/"+idClient;
    }
}
