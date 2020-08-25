package com.pwc.modules.sys.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.sys.entity.FilingThirdCityCodeEntity;
import com.pwc.modules.sys.entity.FilingThirdDistrictEntity;
import com.pwc.modules.sys.service.FilingThirdCityCodeService;
import com.pwc.modules.sys.service.FilingThirdDistrictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.sys.entity.SysDistrictEntity;
import com.pwc.modules.sys.service.SysDistrictService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;



/**
 * 行政区字典
 *
 * @author zk
 * @email 
 * @date 2019-11-13 18:54:25
 */
@RestController
@RequestMapping("sys/district")
public class SysDistrictController {
    @Autowired
    private SysDistrictService sysDistrictService;
    @Autowired
    private FilingThirdDistrictService filingThirdDistrictService;
    @Autowired
    private FilingThirdCityCodeService filingThirdCityCodeService;


    /**
     * 已匹配的citycode
     * @param thirdCityCode
     * @return
     */
    @GetMapping("/getCityCodeByThird")
    public R getThirdCityCode(String thirdCityCode) {
        List<FilingThirdCityCodeEntity> filingThirdCityCodeList = filingThirdCityCodeService.list(new QueryWrapper<FilingThirdCityCodeEntity>().eq("third_city_code", thirdCityCode));
        return R.ok().put("data", filingThirdCityCodeList);
    }

    @PutMapping("/saveThirdCityCode")
    public R saveThirdCityCode(@RequestBody  FilingThirdCityCodeEntity thirdCityCodeEntity) {
        filingThirdCityCodeService.remove(new QueryWrapper<FilingThirdCityCodeEntity>().eq("third_city_code", thirdCityCodeEntity.getThirdCityCode()));
        for (String cityCode : thirdCityCodeEntity.getCityCodeList()) {
            FilingThirdCityCodeEntity filingThirdCityCodeEntity = new FilingThirdCityCodeEntity();
            filingThirdCityCodeEntity.setThirdCityCode(thirdCityCodeEntity.getThirdCityCode());
            filingThirdCityCodeEntity.setCityCode(cityCode);
            filingThirdCityCodeService.save(filingThirdCityCodeEntity);
        }
        return R.ok();
    }
    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:district:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysDistrictService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @GetMapping("/thirdCityList")
    @RequiresPermissions("sys:district:list")
    public R thirdCityList(@RequestParam Map<String, Object> params){
        PageUtils page = filingThirdDistrictService.queryPage(params);

        for (Object object : page.getList()) {
            FilingThirdDistrictEntity filingThirdDistrictEntity = (FilingThirdDistrictEntity) object;
            //查询第三方关联code
            List<Object> cityCodes = filingThirdCityCodeService.listObjs(new QueryWrapper<FilingThirdCityCodeEntity>().select("city_code").eq("third_city_code", filingThirdDistrictEntity.getCityCode()));
            // 不为空查询关联的名称
            if (CollUtil.isNotEmpty(cityCodes)) {
                List<Object> cityNames = sysDistrictService.listObjs(new QueryWrapper<SysDistrictEntity>().select("name").in("code", cityCodes));
                filingThirdDistrictEntity.setSetCityNames(Convert.toList(String.class,cityNames));
            }
        }
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @GetMapping("/treeList")
    @RequiresPermissions("sys:district:list")
    public R list(String thirdCityCode){
        Long districtId = 0L;
        if (StrUtil.isNotBlank(thirdCityCode)) {
            String cityCode;
            if (thirdCityCode.length() < 4) {
                cityCode = thirdCityCode+"0000";
            } else {
                cityCode = thirdCityCode+"00";
            }
            List<Object> obj = sysDistrictService.listObjs(new QueryWrapper<SysDistrictEntity>().select("id").eq("code", cityCode));
            List<Long> ids = Convert.toList(Long.class, obj);
            if (CollUtil.isNotEmpty(ids)) {
                districtId = ids.get(0);
            }
        }
        List<SysDistrictEntity> districtList = sysDistrictService.getSubDistrictList(districtId);
        if (districtId != 0) {
            districtList.add(sysDistrictService.queryById(districtId));
        }

        return R.ok().put("data", districtList);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:district:info")
    public R info(@PathVariable("id") Integer id){
        SysDistrictEntity sysDistrict = sysDistrictService.getById(id);

        return R.ok().put("district", sysDistrict);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    @RequiresPermissions("sys:district:save")
    public R save(@RequestBody SysDistrictEntity sysDistrict){
        sysDistrictService.save(sysDistrict);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:district:update")
    public R update(@RequestBody SysDistrictEntity sysDistrict){
        ValidatorUtils.validateEntity(sysDistrict);
        sysDistrictService.updateById(sysDistrict);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("sys:district:delete")
    public R delete(@RequestBody Integer[] ids){
        sysDistrictService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
