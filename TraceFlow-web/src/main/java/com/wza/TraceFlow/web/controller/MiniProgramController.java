package com.wza.TraceFlow.web.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.http.HttpUtil;
import com.google.common.base.Throwables;
import com.wza.TraceFlow.common.enums.RespStatusEnum;
import com.wza.TraceFlow.common.vo.BasicResultVO;
import com.wza.TraceFlow.support.utils.AccountUtils;
import com.wza.TraceFlow.web.utils.Convert4Amis;
import com.wza.TraceFlow.web.vo.amis.CommonAmisVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.subscribemsg.TemplateInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 微信服务号
 *
 * @author 3y
 */
@Slf4j
@RestController
@RequestMapping("/miniProgram")
@Api("微信服务号")
public class MiniProgramController {

    @Autowired
    private AccountUtils accountUtils;

    @GetMapping("/template/list")
    @ApiOperation("/根据账号Id获取模板列表")
    public BasicResultVO queryList(Integer id) {
        try {
            List<CommonAmisVo> result = new ArrayList<>();
            WxMaService wxMaService = accountUtils.getAccountById(id, WxMaService.class);
            List<TemplateInfo> templateList = wxMaService.getSubscribeService().getTemplateList();
            for (TemplateInfo templateInfo : templateList) {
                CommonAmisVo commonAmisVo = CommonAmisVo.builder().label(templateInfo.getTitle()).value(templateInfo.getPriTmplId()).build();
                result.add(commonAmisVo);
            }
            return BasicResultVO.success(result);
        } catch (Exception e) {
            log.error("MiniProgramController#queryList fail:{}", Throwables.getStackTraceAsString(e));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR);
        }

    }

    /**
     * 根据账号Id和模板ID获取模板列表
     *
     * @return
     */
    @PostMapping("/detailTemplate")
    @ApiOperation("/根据账号Id和模板ID获取模板列表")
    public BasicResultVO queryDetailList(Integer id, String wxTemplateId) {
        if (Objects.isNull(id) || Objects.isNull(wxTemplateId)) {
            return BasicResultVO.success(RespStatusEnum.CLIENT_BAD_PARAMETERS);
        }
        try {
            WxMaService wxMaService = accountUtils.getAccountById(id, WxMaService.class);
            List<TemplateInfo> templateList = wxMaService.getSubscribeService().getTemplateList();
            CommonAmisVo wxMpTemplateParam = Convert4Amis.getWxMaTemplateParam(wxTemplateId, templateList);
            return BasicResultVO.success(wxMpTemplateParam);
        } catch (Exception e) {
            log.error("MiniProgramController#queryDetailList fail:{}", Throwables.getStackTraceAsString(e));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR);
        }
    }

    /**
     * 登录凭证校验
     * <p>
     * 临时给小程序登录使用，正常消息推送平台不会有此接口
     *
     * @return
     */
    @GetMapping("/sync/openid")
    @ApiOperation("登录凭证校验")
    public BasicResultVO syncOpenId(String code, String appId, String secret) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String result = HttpUtil.get(url);
        return BasicResultVO.success(result);
    }

}
