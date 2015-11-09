package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.AdminInterventionDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.service.AmountTransferService;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static com.tuotiansudai.repository.model.UserBillBusinessType.ADMIN_INTERVENTION;

@Controller
@RequestMapping(path = "/admin-intervention")
public class AdminInterventionController {
    static Logger logger = Logger.getLogger(AdminInterventionController.class);

    @Autowired
    private AmountTransferService amountTransferService;

    @Autowired
    private IdGenerator idGenerator;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView intervene(Model model) {
        ModelAndView modelAndView = new ModelAndView("/admin-intervention");
        modelAndView.addObject("operationTypes", Lists.newArrayList(UserBillOperationType.values()));
        modelAndView.addObject("data", model.containsAttribute("data") ? model.asMap().get("data") : new AdminInterventionDto());
        if (model.containsAttribute("message")) {
            modelAndView.addObject("message", model.asMap().get("message"));
        }
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView intervene(@Valid @ModelAttribute AdminInterventionDto adminInterventionDto, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("redirect:/admin-intervention");

        long orderId = idGenerator.generate();

        try {
            String loginName = adminInterventionDto.getLoginName();
            long amount = AmountUtil.convertStringToCent(adminInterventionDto.getAmount());
            String description = adminInterventionDto.getDescription();
            switch (adminInterventionDto.getOperationType()) {
                case TI_BALANCE:
                    amountTransferService.transferInBalance(loginName, orderId, amount, ADMIN_INTERVENTION, LoginUserInfo.getLoginName(), description);
                    break;
                case TO_BALANCE:
                    amountTransferService.transferOutFreeze(loginName, orderId, amount, ADMIN_INTERVENTION, LoginUserInfo.getLoginName(), description);
                    break;
                case FREEZE:
                    amountTransferService.freeze(loginName, orderId, amount, ADMIN_INTERVENTION, LoginUserInfo.getLoginName(), description);
                    break;
                case UNFREEZE:
                    amountTransferService.unfreeze(loginName, orderId, amount, ADMIN_INTERVENTION, LoginUserInfo.getLoginName(), description);
                    break;
                case TO_FREEZE:
                    amountTransferService.transferOutFreeze(loginName, orderId, amount, ADMIN_INTERVENTION, LoginUserInfo.getLoginName(), description);
                    break;
            }
            redirectAttributes.addFlashAttribute("message", "修改成功");
        } catch (AmountTransferException e) {
            logger.error(e.getLocalizedMessage(), e);
            redirectAttributes.addFlashAttribute("data", adminInterventionDto);
            redirectAttributes.addFlashAttribute("message", "金额不足");
        }

        return modelAndView;
    }
}
