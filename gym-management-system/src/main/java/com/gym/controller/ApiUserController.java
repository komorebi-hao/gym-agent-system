package com.gym.controller;

import com.gym.pojo.ClassOrder;
import com.gym.pojo.ClassTable;
import com.gym.pojo.Member;
import com.gym.service.ClassOrderService;
import com.gym.service.ClassTableService;
import com.gym.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class ApiUserController {

    @Autowired
    private ClassTableService classTableService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ClassOrderService classOrderService;

    @GetMapping("/toUserInfo")
    public Map<String, Object> toUserInfo(HttpSession session) {
        Member member = (Member) session.getAttribute("user");
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("member", member);
        return resp;
    }

    @GetMapping("/toUpdateInfo")
    public Map<String, Object> toUpdateInfo(HttpSession session) {
        Member member = (Member) session.getAttribute("user");
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("member", member);
        return resp;
    }

    @PostMapping("/updateInfo")
    public ResponseEntity<Map<String, Object>> updateUserInformation(HttpSession session, Member member) {
        Member member1 = (Member) session.getAttribute("user");
        if (member1 != null && member != null) {
            member.setMemberAccount(member1.getMemberAccount());
            member.setCardClass(member1.getCardClass());
            member.setCardTime(member1.getCardTime());
            member.setCardNextClass(member1.getCardNextClass());

            memberService.updateMemberByMemberAccount(member);
            session.setAttribute("user", member);
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/toUserClass")
    public Map<String, Object> toUserClass(HttpSession session) {
        Member member = (Member) session.getAttribute("user");
        Integer memberAccount = member == null ? null : member.getMemberAccount();
        List<ClassOrder> classOrderList = classOrderService.selectClassOrderByMemberAccount(memberAccount);

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("member", member);
        resp.put("classOrderList", classOrderList);
        return resp;
    }

    @PostMapping("/delUserClass")
    public ResponseEntity<Map<String, Object>> deleteUserClass(Integer classOrderId) {
        classOrderService.deleteByClassOrderId(classOrderId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/toApplyClass")
    public Map<String, Object> toUserApplyClass(HttpSession session) {
        Member member = (Member) session.getAttribute("user");
        List<ClassTable> classList = classTableService.findAll();

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("member", member);
        resp.put("classList", classList);
        return resp;
    }

    @PostMapping("/applyClass")
    public ResponseEntity<Map<String, Object>> userApplyClass(Integer classId, HttpSession session) {
        ClassTable classTable = classTableService.selectByClassId(classId);
        Member member = (Member) session.getAttribute("user");

        if (classTable == null || member == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", false);
            resp.put("message", "会话已失效或课程不存在");
            return ResponseEntity.ok(resp);
        }

        String className = classTable.getClassName();
        String coach = classTable.getCoach();
        String classBegin = classTable.getClassBegin();
        String memberName = member.getMemberName();
        Integer memberAccount = member.getMemberAccount();

        ClassOrder classOrder = new ClassOrder(classId, className, coach, memberName, memberAccount, classBegin);

        ClassOrder classOrder1 = classOrderService.selectMemberByClassIdAndMemberAccount(classId, memberAccount);
        if (classOrder1 == null) {
            classOrderService.insertClassOrder(classOrder);
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        return ResponseEntity.ok(resp);
    }
}

