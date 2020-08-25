package com.pwc.modules.filing.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.pwc.common.utils.FilingConstants;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.filing.entity.FilingNodeEntity;
import com.pwc.modules.filing.service.FilingNodeService;
import com.pwc.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 申报节点设置
 *
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
@RestController
@RequestMapping("filing/node")
public class FilingNodeController extends AbstractController {

    @Autowired
    private FilingNodeService filingNodeService;

    /**
     * 列表
     * @param params 参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("filing:node:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = filingNodeService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 当前登录公司的节点时间
     */
    @GetMapping("/info")
    @RequiresPermissions("filing:node:info")
    public R info(){
        String datePattern = "yyyy-MM";
        String currentDateStr = DateUtil.format(DateUtil.date(), datePattern);

        FilingNodeEntity filingNode = filingNodeService.queryByDeptAndCreateTime(getDeptId(),currentDateStr);

        return R.ok().put("filingNode", filingNode);
    }



    /**
     * 信息
     */
    @GetMapping("/info/{nodeId}")
    @RequiresPermissions("filing:node:info")
    public R info(@PathVariable("nodeId") Long nodeId){
        FilingNodeEntity filingNode = filingNodeService.getById(nodeId);

        return R.ok().put("filingNode", filingNode);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("filing:node:save")
    public R save(@RequestBody FilingNodeEntity filingNode){
        ValidatorUtils.validateEntity(filingNode);
        String datePattern = "yyyy-MM";
        String currentDateStr = DateUtil.format(DateUtil.date(), datePattern);

        if(null != filingNodeService.queryByDeptAndCreateTime(getDeptId(),currentDateStr)) {
            return R.error("每个月只能设置一次");
        }
        filingNode.setDeptId(getDeptId());
        filingNodeService.saveFilingNote(filingNode);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("filing:node:update")
    public R update(@RequestBody FilingNodeEntity filingNode){
        ValidatorUtils.validateEntity(filingNode);
        String datePattern = "yyyy-MM";
        String currentDateStr = DateUtil.format(DateUtil.date(), datePattern);
        FilingNodeEntity currentMonthNode = filingNodeService.queryByDeptAndCreateTime(filingNode.getDeptId(),currentDateStr);
        if(null != currentMonthNode && currentMonthNode.getStatus().equals(FilingConstants.NodeStatus.MODIFIED.getValue())) {
            return R.error("每个月只能设置一次");
        }
        filingNode.setStatus(FilingConstants.NodeStatus.MODIFIED.getValue());
        filingNodeService.updateById(filingNode);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("filing:node:delete")
    public R delete(@RequestBody Long[] nodeIds){
        filingNodeService.removeByIds(Arrays.asList(nodeIds));

        return R.ok();
    }

}
