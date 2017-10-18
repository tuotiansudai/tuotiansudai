package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.console.dto.AdminInterventionDto;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.AmountTransferMultiMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
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

import static com.tuotiansudai.enums.UserBillBusinessType.ADMIN_INTERVENTION;

@Controller
@RequestMapping(path = "/finance-manage/admin-intervention")
public class AdminInterventionController {
    static Logger logger = Logger.getLogger(AdminInterventionController.class);

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private UserMapper userMapper;

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
        ModelAndView modelAndView = new ModelAndView("redirect:/finance-manage/admin-intervention");

        long orderId = IdGenerator.generate();

        String mobile = adminInterventionDto.getMobile();
        UserModel userModel = userMapper.findByMobile(mobile);
        String loginName = userModel.getLoginName();
        long amount = AmountConverter.convertStringToCent(adminInterventionDto.getAmount());
        String description = adminInterventionDto.getDescription();

        AmountTransferMessage atm = new AmountTransferMessage(null, loginName, orderId, amount, ADMIN_INTERVENTION, LoginUserInfo.getLoginName(), description);
        switch (adminInterventionDto.getOperationType()) {
            case TI_BALANCE:
                atm.setTransferType(TransferType.TRANSFER_IN_BALANCE);
                break;
            case TO_BALANCE:
                atm.setTransferType(TransferType.TRANSFER_OUT_BALANCE);
                break;
            case FREEZE:
                atm.setTransferType(TransferType.FREEZE);
                break;
            case UNFREEZE:
                atm.setTransferType(TransferType.UNFREEZE);
                break;
            case TO_FREEZE:
                atm.setTransferType(TransferType.TRANSFER_OUT_FREEZE);
                break;
        }
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, new AmountTransferMultiMessage(atm));
        redirectAttributes.addFlashAttribute("message", "修改成功");

        return modelAndView;
    }
}
