package com.george.reggie_delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.george.reggie_delivery.common.R;
import com.george.reggie_delivery.common.ThreadLocalUtils;
import com.george.reggie_delivery.entity.AddressBook;
import com.george.reggie_delivery.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author George
 * @Date 2022-06-24-11:19
 * @Description TODO
 * @Version 1.0
 */
@RestController
@ResponseBody
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public R<AddressBook> saveAddress(@RequestBody AddressBook addressBook){
        Long userId = ThreadLocalUtils.getValue();
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    @GetMapping("/list")
    public R<List<AddressBook>> getAddress(){
        Long userId = ThreadLocalUtils.getValue();
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(userId!=null,AddressBook::getUserId,userId)
        .orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> addressBookList = addressBookService.list(addressBookLambdaQueryWrapper);
        return R.success(addressBookList);
    }

    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, ThreadLocalUtils.getValue());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }
    @PutMapping("/default")
    public R<AddressBook> defaultAddress(@RequestBody  AddressBook addressBook){
        if(addressBook != null){
            Long userId = ThreadLocalUtils.getValue();
            LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(userId!=null,AddressBook::getUserId,userId)
            .set(AddressBook::getIsDefault,0);
            addressBookService.update(updateWrapper);
            LambdaUpdateWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaUpdateWrapper<>();
            addressBookLambdaQueryWrapper.eq(addressBook.getId()!=null,AddressBook::getId,addressBook.getId())
            .set(AddressBook::getIsDefault,1);
            addressBookService.update(addressBookLambdaQueryWrapper);
        }
        log.info(addressBook.toString());
        return R.success(addressBook);
    }

    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable("id") Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    @PutMapping
    public R<String> updateAddress(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return R.success("更新成功");
    }

    @DeleteMapping
    public R<String> deleteById(@RequestParam("ids") Long id){
        addressBookService.removeById(id);
        return R.success("删除成功");
    }
}
