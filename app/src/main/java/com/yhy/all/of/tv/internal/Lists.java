package com.yhy.all.of.tv.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created on 2022-10-27 10:23
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class Lists {

    /**
     * 快速创建一个 List
     * <p>
     * 用 {@link ArrayList} 实现
     *
     * @param t   元素列表
     * @param <T> 元素类型
     * @return List
     */
    @SafeVarargs
    public static <T> List<T> of(T... t) {
        List<T> list = new ArrayList<>();
        if (null != t && t.length > 0) {
            list.addAll(Arrays.asList(t));
        }
        return list;
    }

    /**
     * 求两个或两个以上元素集合的交集
     * <p>
     * 使用 {@link List#contains(Object)} 方式判断
     *
     * @param list1 集合1
     * @param list2 集合2
     * @param lists 更多集合
     * @param <T>   元素类型
     * @return 交集
     */
    @SafeVarargs
    public static <T> List<T> intersection(List<T> list1, List<T> list2, List<T>... lists) {
        List<T> result = intersection(list1, list2, list2::contains);
        if (null != lists && lists.length > 0) {
            for (List<T> list : lists) {
                result = intersection(result, list, list::contains);
            }
        }
        return result;
    }

    /**
     * 求两个或两个以上元素集合的交集
     *
     * @param predicate 条件匹配器
     * @param list1     集合1
     * @param list2     集合2
     * @param lists     更多集合
     * @param <T>       元素类型
     * @return 交集
     */
    @SafeVarargs
    public static <T> List<T> intersection(BiPredicate<T, T> predicate, List<T> list1, List<T> list2, List<T>... lists) {
        List<T> result = intersection(list1, list2, item1 -> list2.stream().anyMatch(item2 -> predicate.test(item1, item2)));
        if (null != lists && lists.length > 0) {
            for (List<T> list : lists) {
                result = intersection(result, list, item1 -> list.stream().anyMatch(item2 -> predicate.test(item1, item2)));
            }
        }
        return result;
    }

    /**
     * 求两个集合的交集
     * <p>
     * 匹配表达式 {@link Predicate#test(Object)}
     *
     * @param list1     集合1
     * @param list2     集合2
     * @param predicate 匹配表达式
     * @param <T>       元素类型
     * @return 交集
     */
    public static <T> List<T> intersection(List<T> list1, List<T> list2, Predicate<T> predicate) {
        if (isEmpty(list1)) {
            return isEmpty(list2) ? null : new ArrayList<>(list2);
        }
        if (isEmpty(list2)) {
            return isEmpty(list1) ? null : new ArrayList<>(list1);
        }
        return list1.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 多个集合的并集，结果集不去重
     *
     * @param lists 多个集合参数
     * @param <T>   元素类型
     * @return 并集
     */
    @SafeVarargs
    public static <T> List<T> union(List<T>... lists) {
        return union(false, lists);
    }

    /**
     * 多个集合的并集，结果集支持去重
     *
     * @param distinct 结果集是否去重
     * @param lists    多个集合参数
     * @param <T>      元素类型
     * @return 并集
     */
    @SafeVarargs
    public static <T> List<T> union(boolean distinct, List<T>... lists) {
        if (null == lists || lists.length == 0) {
            return null;
        }
        List<T> result = new ArrayList<>();
        for (List<T> list : lists) {
            if (isNotEmpty(list)) {
                result.addAll(list);
            }
        }
        if (distinct) {
            return result.stream().distinct().collect(Collectors.toList());
        }
        return result;
    }

    /**
     * list 关于 u 的相对补集，差集
     * <p>
     * 两个集合类型相同
     * <p>
     * 使用 {@link List#contains(Object)} 方式判断
     *
     * @param u    全集
     * @param list 任意集合
     * @param <T>  元素类型
     * @return 补集
     */
    public static <T> List<T> difference(List<T> u, List<T> list) {
        // 返回 u 中存在 但 list 中不存在的元素集合
        return difference(u, list, itemU -> !list.contains(itemU));
    }

    /**
     * list 关于 u 的相对补集，差集
     * <p>
     * 两个集合类型可以不同，返回值类型与全集类型相同
     *
     * @param u         全集
     * @param list      任意集合
     * @param predicate 匹配器
     * @param <S>       全集元素类型
     * @param <T>       集合元素类型
     * @return 补集
     */
    public static <S, T> List<S> difference(List<S> u, List<T> list, BiPredicate<S, T> predicate) {
        return difference(u, list, itemU -> list.stream().noneMatch(item -> predicate.test(itemU, item)));
    }

    /**
     * list 关于 u 的相对补集，差集
     * <p>
     * 两个集合类型可以不同，返回值类型与全集类型相同
     * <p>
     * 匹配表达式 {@link Predicate#test(Object)}
     *
     * @param u         全集
     * @param list      任意集合
     * @param predicate 匹配表达式
     * @param <S>       全集元素类型
     * @param <T>       集合元素类型
     * @return 补集
     */
    public static <S, T> List<S> difference(List<S> u, List<T> list, Predicate<S> predicate) {
        if (isEmpty(u)) {
            return null;
        }
        if (isEmpty(list)) {
            return isEmpty(u) ? null : new ArrayList<>(u);
        }
        // 返回 u 中存在 但 list 中不存在的元素集合
        return u.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 判断集合为空
     *
     * @param list 集合
     * @param <T>  元素类型
     * @return 是否为空
     */
    public static <T> boolean isEmpty(List<T> list) {
        return null == list || list.isEmpty();
    }

    /**
     * 判断集合不为空
     *
     * @param list 集合
     * @param <T>  元素类型
     * @return 是否不为空
     */
    public static <T> boolean isNotEmpty(List<T> list) {
        return !isEmpty(list);
    }

    /**
     * List 集合中是否包含某个元素
     * <p>
     * 使用 {@link Objects#equals(Object, Object)} 方式判断
     *
     * @param src     集合
     * @param element 元素
     * @param <T>     元素类型
     * @return 是否包含
     */
    public static <T> boolean contains(List<T> src, T element) {
        return contains(src, element, Objects::equals);
    }

    /**
     * List 集合中是否包含某个元素
     *
     * @param src       集合
     * @param element   元素
     * @param predicate 匹配器
     * @param <T>       元素类型
     * @return 是否包含
     */
    public static <T> boolean contains(List<T> src, T element, BiPredicate<T, T> predicate) {
        return isEmpty(src) || null == element || src.stream().anyMatch(item -> predicate.test(item, element));
    }

    /**
     * 字符串拼接
     *
     * @param iterable  原始数据集
     * @param delimiter 连接字符
     * @param <T>       数据类型
     * @return 拼接结果
     */
    public static <T> String join(Iterable<T> iterable, String delimiter) {
        StringJoiner sj = new StringJoiner(delimiter);
        if (null != iterable) {
            iterable.forEach(it -> sj.add(it.toString()));
        }
        return sj.toString();
    }

    public static void main(String[] args) {
        System.out.println("\n=========================================== 基本类型 ===========================================\n");

        List<String> list1 = Lists.of("A", "B", "C", "D", null);
        List<String> list2 = Lists.of("C", "D", "E", "F", "G", null);

        System.out.println("包含：" + contains(list1, "A"));
        System.out.println("不包含：" + contains(list1, "E"));
        System.out.println("交集：" + intersection(list1, list2));
        System.out.println("并集（默认）：" + union(list1, list2));
        System.out.println("并集（去重）：" + union(true, list1, list2));
        System.out.println("差集（1中有2中无）：" + difference(list1, list2));
        System.out.println("差集（2中有1中无）：" + difference(list2, list1));

        System.out.println("\n=========================================== 复杂类型 ===========================================\n");
        System.out.println("\033[1;31m---- 错误示例：\033[m\n");

        List<TestEntry> listA = Lists.of(
                TestEntry.create(1, "第一"),
                TestEntry.create(2, "第二"),
                TestEntry.create(3, "第三"),
                TestEntry.create(4, "第四")
        );

        List<TestEntry> listB = Lists.of(
                TestEntry.create(3, "第三"),
                TestEntry.create(4, "第四"),
                TestEntry.create(5, "第五"),
                TestEntry.create(6, "第六"),
                TestEntry.create(7, "第七")
        );

        List<TestEntry> listC = Lists.of(
                TestEntry.create(3, "第三"),
                TestEntry.create(5, "第五"),
                TestEntry.create(6, "第六"),
                TestEntry.create(8, "第⑧"),
                TestEntry.create(7, "第七")
        );

        System.out.println("包含：" + contains(listA, TestEntry.create(1, "第一")));
        System.out.println("不包含：" + contains(listA, TestEntry.create(5, "第五")));
        System.out.println("交集：" + intersection(listA, listB));
        System.out.println("并集（默认）：" + union(listA, listB));
        System.out.println("并集（去重）：" + union(true, listA, listB));
        System.out.println("差集（A中有B中无）：" + difference(listA, listB));
        System.out.println("差集（B中有A中无）：" + difference(listB, listA));

        System.out.println("\n\033[1;36m---- 正确示例：\033[m\n");

        System.out.println("包含：" + contains(listA, TestEntry.create(1, "第一"), (testEntry, testEntry2) -> testEntry.id.equals(testEntry2.id)));
        System.out.println("不包含：" + contains(listA, TestEntry.create(5, "第五"), (testEntry, testEntry2) -> testEntry.id.equals(testEntry2.id)));
        System.out.println("A∩B - 交集：" + intersection((testEntry, testEntry2) -> testEntry.id.equals(testEntry2.id), listA, listB));
        System.out.println("B∩C - 交集：" + intersection((testEntry, testEntry2) -> testEntry.id.equals(testEntry2.id), listB, listC));
        System.out.println("A∩B∩C - 交集：" + intersection((testEntry, testEntry2) -> testEntry.id.equals(testEntry2.id), listA, listB, listC));
        System.out.println("A∪B - 并集（默认）：" + union(listA, listB));
        System.out.println("A∪B - 并集（去重）：" + union(true, listA, listB));
        System.out.println("B∪C - 并集（默认）：" + union(listB, listC));
        System.out.println("A∪B∪C - 并集（默认）：" + union(listA, listB, listC));
        System.out.println("差集（A中有B中无）：" + difference(listA, listB, (testEntry, testEntry2) -> testEntry.id.equals(testEntry2.id)));
        System.out.println("差集（B中有A中无）：" + difference(listB, listA, (testEntry, testEntry2) -> testEntry.id.equals(testEntry2.id)));
    }

    private static class TestEntry {
        Integer id;
        String name;

        static TestEntry create(Integer id, String name) {
            TestEntry entry = new TestEntry();
            entry.id = id;
            entry.name = name;
            return entry;
        }

        @Override
        public String toString() {
            return "TestEntry{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
