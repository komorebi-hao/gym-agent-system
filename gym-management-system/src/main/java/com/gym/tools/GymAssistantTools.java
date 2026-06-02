package com.gym.tools;

import com.gym.pojo.ClassOrder;
import com.gym.pojo.ClassTable;
import com.gym.pojo.Member;
import com.gym.service.ClassOrderService;
import com.gym.service.ClassTableService;
import com.gym.service.MemberService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GymAssistantTools {

    @Autowired
    private ClassOrderService classOrderService;

    @Autowired
    private ClassTableService classTableService;

    @Autowired
    private MemberService memberService;

    /**
     * 根据会员账号查询已经报名的课程
     *
     * @param memberAccount 会员账号
     * @return 已报名课程列表信息
     */
    @Tool(name = "查询已报名课程", value = "根据会员账号查询该会员已经报名的所有课程信息")
    public String queryEnrolledClasses(@P(value = "会员账号")Integer memberAccount) {
        if (memberAccount == null) {
            return "会员账号不能为空";
        }

        // 验证会员是否存在
        Member member = memberService.selectByMemberAccount(memberAccount);
        if (member == null) {
            return "未找到会员账号为 " + memberAccount + " 的会员信息";
        }


        List<ClassOrder> classOrders = classOrderService.selectClassOrderByMemberAccount(memberAccount);

        if (classOrders == null || classOrders.isEmpty()) {
            return "会员 " + member.getMemberName() + "（账号：" + memberAccount + "）暂未报名任何课程";
        }

        StringBuilder result = new StringBuilder();
        result.append("会员 ").append(member.getMemberName()).append("（账号：").append(memberAccount).append("）已报名的课程：\n");
        result.append("共报名 ").append(classOrders.size()).append(" 门课程\n\n");

        for (int i = 0; i < classOrders.size(); i++) {
            ClassOrder order = classOrders.get(i);
            result.append("【课程 ").append(i + 1).append("】\n");
            result.append("  报名ID: ").append(order.getClassOrderId()).append("\n");
            result.append("  课程ID: ").append(order.getClassId()).append("\n");
            result.append("  课程名称: ").append(order.getClassName()).append("\n");
            result.append("  教练: ").append(order.getCoach()).append("\n");
            result.append("  开课时间: ").append(order.getClassBegin()).append("\n");
            if (i < classOrders.size() - 1) {
                result.append("\n");
            }
        }

        return result.toString();
    }

    @Tool(name = "获取会员基本信息",value = "根据系统提供的memberAccount获取会员的基本信息")
    public String getMemberInfo(@P(value = "会员账号") Integer memberAccount){
        Member member = memberService.selectByMemberAccount(memberAccount);
        if (member==null){
            return "未找到会员账号为 " + memberAccount + " 的会员信息";
        }
        StringBuilder result = new StringBuilder();
        result.append("会员名字是:").append(member.getMemberName())
                .append("会员年龄是:").append(member.getMemberAge())
                .append("会员性别是：").append(member.getMemberGender())
                .append("会员身高是:").append(member.getMemberHeight())
                .append("会员体重是:").append(member.getMemberWeight());
        return result.toString();

    }

    /**
     * 查询所有开课课程
     *
     * @return 所有开课课程列表
     */
    @Tool(name = "查询所有开课课程", value = "查询健身房所有开课的课程信息")
    public String queryAllClasses() {
        List<ClassTable> classes = classTableService.findAll();

        if (classes == null || classes.isEmpty()) {
            return "当前暂无开课课程";
        }

        StringBuilder result = new StringBuilder();
        result.append("健身房当前开课课程列表：\n");
        result.append("共 ").append(classes.size()).append(" 门课程\n\n");

        for (int i = 0; i < classes.size(); i++) {
            ClassTable classTable = classes.get(i);
            result.append("【课程 ").append(i + 1).append("】\n");
            result.append("  课程ID: ").append(classTable.getClassId()).append("\n");
            result.append("  课程名称: ").append(classTable.getClassName()).append("\n");
            result.append("  教练: ").append(classTable.getCoach()).append("\n");
            result.append("  开课日期: ").append(classTable.getClassBegin()).append("\n");
            result.append("  课程时长: ").append(classTable.getClassTime()).append("\n");
            if (i < classes.size() - 1) {
                result.append("\n");
            }
        }

        return result.toString();
    }

    /**
     * 报名课程
     *
     * @param memberAccount 会员账号
     * @param className      课程名称
     * @return 报名结果
     */
    @Tool(name = "报名课程", value = "会员账号报名指定课程")
    public String enrollClass(@P(value = "会员账号")Integer memberAccount, @P(value = "课程名称")String className) {
        if (memberAccount == null || className == null) {
            return "会员账号和课程名称不能为空";
        }

        // 验证会员是否存在
        Member member = memberService.selectByMemberAccount(memberAccount);
        if (member == null) {
            return "未找到会员账号为 " + memberAccount + " 的会员信息";
        }


        // 验证课程是否存在
        ClassTable classTable = classTableService.selectByClassName(className);
        if (classTable == null) {
            return "未找到课程名称为 " + className + " 的课程信息";
        }

        // 检查会员是否已报名该课程
        ClassOrder existingOrder = classOrderService.selectMemberByClassNameAndMemberAccount(className, memberAccount);
        if (existingOrder != null) {
            return "会员 " + member.getMemberName() + " 已报名课程《" + classTable.getClassName() + "》，无需重复报名";
        }

        // 检查会员卡次数是否充足
        Integer remainingClasses = member.getCardNextClass();
        if (remainingClasses == null || remainingClasses <= 0) {
            return "会员 " + member.getMemberName() + " 的卡次不足，剩余次数：" +
                   (remainingClasses == null ? 0 : remainingClasses) + "，无法报名新课程";
        }

        // 创建报名记录
        ClassOrder classOrder = new ClassOrder(
                classTable.getClassId(),
                className,
                classTable.getCoach(),
                member.getMemberName(),
                memberAccount,
                classTable.getClassBegin()
        );

        Boolean success = classOrderService.insertClassOrder(classOrder);
        if (success) {
            // 更新会员卡次数
            member.setCardNextClass(remainingClasses - 1);
            memberService.updateMemberByMemberAccount(member);

            return "报名成功！\n" +
                   "会员：" + member.getMemberName() + "（账号：" + memberAccount + "）\n" +
                   "课程：《" + classTable.getClassName() + "》\n" +
                   "教练：" + classTable.getCoach() + "\n" +
                   "开课时间：" + classTable.getClassBegin() + "\n" +
                   "剩余卡次：" + (remainingClasses - 1);
        } else {
            return "报名失败，请稍后重试或联系管理员";
        }
    }

    /**
     * 退课功能
     *
     * @param memberAccount 会员账号
     * @param className      课程名称
     * @return 退课结果
     */
    @Tool(name = "退课", value = "为指定会员账号退订指定课程")
    public String cancelClass(@P(value = "会员账号")Integer memberAccount, @P(value = "课程名称")String className) {
        if (memberAccount == null || className == null) {
            return "会员账号和课程ID不能为空";
        }

        // 验证会员是否存在
        Member member = memberService.selectByMemberAccount(memberAccount);
        if (member == null) {
            return "未找到会员账号为 " + memberAccount + " 的会员信息";
        }


        // 验证课程是否存在
        ClassTable classTable = classTableService.selectByClassName(className);
        if (classTable == null) {
            return "未找到课程名称为 " + className + " 的课程信息";
        }

        // 查询该会员是否报名了该课程
        ClassOrder existingOrder = classOrderService.selectMemberByClassNameAndMemberAccount(className, memberAccount);
        if (existingOrder == null) {
            return "会员 " + member.getMemberName() + " 未报名课程《" + className + "》，无需退课";
        }

        // 执行退课
        Boolean success = classOrderService.deleteByClassOrderId(existingOrder.getClassOrderId());
        if (success) {
            // 返还卡次数
            Integer remainingClasses = member.getCardNextClass();
            member.setCardNextClass(remainingClasses == null ? 1 : remainingClasses + 1);
            memberService.updateMemberByMemberAccount(member);

            return "退课成功！\n" +
                   "会员：" + member.getMemberName() + "（账号：" + memberAccount + "）\n" +
                   "退课课程：《" + classTable.getClassName() + "》\n" +
                   "当前剩余卡次：" + member.getCardNextClass();
        } else {
            return "退课失败，请稍后重试或联系管理员";
        }
    }
}
