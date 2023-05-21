package com.store.book.controller;

import com.store.book.Repository.ClientRepository;
import com.store.book.Repository.TypeClientRepository;
import com.store.book.entity.Client;
import com.store.book.entity.TypeClient;
import com.store.book.service.PagingService;
import com.store.book.util.FileUploadUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/client")
@PreAuthorize("hasRole('ADMIN')")
public class ClientController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    TypeClientRepository typeClientRepository;

    @Autowired
    private PagingService pagingService;

    @RequestMapping(value="/list", method= RequestMethod.GET)
    public String listClient(ModelMap model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, String keyword) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(2);
        List<Client> clients = new ArrayList<Client>();
        if (keyword!=null) {
            clients = clientRepository.findByKeyword(keyword);
        }
        else {
            clients = clientRepository.findAll();
        }
        Page<Client> clientPage = pagingService.findPaginated(PageRequest.of(currentPage - 1, pageSize), clients);
        if (clients != null) {
            model.addAttribute("clientPage", clientPage);
            model.addAttribute("currentPage", currentPage);
        }
        int totalPages = clientPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "clientList";
    }


    @GetMapping(value = {"/create-edit", "/create-edit/{idClient}"})
    public String create(@PathVariable(value = "idClient", required = false) String idClient, Model model) {
        List<TypeClient> typeClients = typeClientRepository.findAll();
        if (typeClients != null) {
            model.addAttribute("typeClients", typeClients);
        }
        if (idClient == null)
            model.addAttribute("client", new Client());
        else
            model.addAttribute("client", clientRepository.findByIdClient(idClient));
        return "createClient";
    }

    @PostMapping("/save")
    public String save(@Valid Client client, BindingResult bindingResult, RedirectAttributes redirect, Model model, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (StringUtils.isBlank(client.getIdClient())) {
            if (bindingResult.hasErrors()) {
                model.addAttribute("client", new Client());
                return "createClient";
            }

            String prefix = "KH";
            Integer id = clientRepository.getMaxRecord();
            if (id == null) id = 0;
            String generatedId = prefix + StringUtils.leftPad(String.valueOf(id + 1), 5, "0");
            client.setIdClient(generatedId);
        }else {
            if (bindingResult.hasErrors()) {
                model.addAttribute("client", client);
                return "createClient";
            }
        }
        String fileName = new String();
        if (StringUtils.isNotBlank(multipartFile.getOriginalFilename())){
            fileName = multipartFile.getOriginalFilename();
            if (fileName != null) client.setPhotos(fileName);
            Client savedUser = clientRepository.save(client);

            String uploadDir = "src/main/resources/static/user-photos/" + savedUser.getIdClient();

            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }else {
            fileName = clientRepository.getNamePhoto(client.getIdClient());
            client.setPhotos(fileName);
            clientRepository.save(client);
        }

        redirect.addFlashAttribute("success", "Saved successfully!");
        return "redirect:/client/list";
    }

//    @GetMapping("/edit/{idClient}")
//    public String edit(@PathVariable String idClient, Model model) {
//        model.addAttribute("client", clientRepository.findByIdClient(idClient));
//        List<TypeClient> typeClients = typeClientRepository.findAll();
//        if (typeClients != null) {
//            model.addAttribute("typeClients", typeClients);
//        }
//        return "createClient";
//    }
//
//    @PostMapping("/update")
//    public String update(Client client, RedirectAttributes redirect) {
//        clientRepository.save(client);
//        redirect.addFlashAttribute("success", "Modified product successfully!");
//        return "redirect:/client/list";
//    }

    @GetMapping("/delete/{idClient}")
    public String delete(@PathVariable String idClient, Model model){
        model.addAttribute("client", clientRepository.findByIdClient(idClient));

        return "deleteClient";
    }

    @PostMapping("/delete")
    @Transactional
    public String delete(Client client, RedirectAttributes redirect) throws IOException {
        String uploadDir = "src/main/resources/static/user-photos/" + client.getIdClient();

        FileUploadUtil.deleteFile(uploadDir);
        clientRepository.deleteByIdClient(client.getIdClient());

        redirect.addFlashAttribute("success", "Removed product successfully!");
        return "redirect:/client/list";
    }

}
