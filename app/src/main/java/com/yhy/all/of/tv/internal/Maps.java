package com.yhy.all.of.tv.internal;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Map 快速构建工具
 * <p>
 * Created on 2021-07-13 10:16
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class Maps {

    /**
     * 快速创建一个 Map
     * <p>
     * 用 {@link HashMap} 实现
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @return a {@code Map} containing the specified mappings
     * @throws InternalError            if the length of input arguments is odd
     * @throws IllegalArgumentException if there are any duplicate keys
     */
    public static <K, V> Map<K, V> of() {
        return mapN();
    }

    /**
     * 快速创建一个 Map
     * <p>
     * 用 {@link HashMap} 实现
     *
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @return a {@code Map} containing the specified mappings
     * @throws InternalError            if the length of input arguments is odd
     * @throws IllegalArgumentException if there are any duplicate keys
     */
    public static <K, V> Map<K, V> of(K k1, V v1) {
        return mapN(k1, v1);
    }

    /**
     * 快速创建一个 Map
     * <p>
     * 用 {@link HashMap} 实现
     *
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @return a {@code Map} containing the specified mappings
     * @throws InternalError            if the length of input arguments is odd
     * @throws IllegalArgumentException if there are any duplicate keys
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        return mapN(k1, v1, k2, v2);
    }

    /**
     * 快速创建一个 Map
     * <p>
     * 用 {@link HashMap} 实现
     *
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @return a {@code Map} containing the specified mappings
     * @throws InternalError            if the length of input arguments is odd
     * @throws IllegalArgumentException if there are any duplicate keys
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return mapN(k1, v1, k2, v2, k3, v3);
    }

    /**
     * 快速创建一个 Map
     * <p>
     * 用 {@link HashMap} 实现
     *
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @return a {@code Map} containing the specified mappings
     * @throws InternalError            if the length of input arguments is odd
     * @throws IllegalArgumentException if there are any duplicate keys
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return mapN(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    /**
     * 快速创建一个 Map
     * <p>
     * 用 {@link HashMap} 实现
     *
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param k5  the fifth mapping's key
     * @param v5  the fifth mapping's value
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @return a {@code Map} containing the specified mappings
     * @throws InternalError            if the length of input arguments is odd
     * @throws IllegalArgumentException if there are any duplicate keys
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return mapN(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    /**
     * 快速创建一个 Map
     * <p>
     * 用 {@link HashMap} 实现
     *
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param k5  the fifth mapping's key
     * @param v5  the fifth mapping's value
     * @param k6  the sixth mapping's key
     * @param v6  the sixth mapping's value
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @return a {@code Map} containing the specified mappings
     * @throws InternalError            if the length of input arguments is odd
     * @throws IllegalArgumentException if there are any duplicate keys
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return mapN(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
    }

    /**
     * 快速创建一个 Map
     * <p>
     * 用 {@link HashMap} 实现
     *
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param k5  the fifth mapping's key
     * @param v5  the fifth mapping's value
     * @param k6  the sixth mapping's key
     * @param v6  the sixth mapping's value
     * @param k7  the seventh mapping's key
     * @param v7  the seventh mapping's value
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @return a {@code Map} containing the specified mappings
     * @throws InternalError            if the length of input arguments is odd
     * @throws IllegalArgumentException if there are any duplicate keys
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return mapN(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
    }

    /**
     * 快速创建一个 Map
     * <p>
     * 用 {@link HashMap} 实现
     *
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param k5  the fifth mapping's key
     * @param v5  the fifth mapping's value
     * @param k6  the sixth mapping's key
     * @param v6  the sixth mapping's value
     * @param k7  the seventh mapping's key
     * @param v7  the seventh mapping's value
     * @param k8  the eighth mapping's key
     * @param v8  the eighth mapping's value
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @return a {@code Map} containing the specified mappings
     * @throws InternalError            if the length of input arguments is odd
     * @throws IllegalArgumentException if there are any duplicate keys
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return mapN(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8);
    }

    /**
     * 快速创建一个 Map
     * <p>
     * 用 {@link HashMap} 实现
     *
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param k5  the fifth mapping's key
     * @param v5  the fifth mapping's value
     * @param k6  the sixth mapping's key
     * @param v6  the sixth mapping's value
     * @param k7  the seventh mapping's key
     * @param v7  the seventh mapping's value
     * @param k8  the eighth mapping's key
     * @param v8  the eighth mapping's value
     * @param k9  the ninth mapping's key
     * @param v9  the ninth mapping's value
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @return a {@code Map} containing the specified mappings
     * @throws InternalError            if the length of input arguments is odd
     * @throws IllegalArgumentException if there are any duplicate keys
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return mapN(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9);
    }

    /**
     * 快速创建一个 Map
     * <p>
     * 用 {@link HashMap} 实现
     *
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param k5  the fifth mapping's key
     * @param v5  the fifth mapping's value
     * @param k6  the sixth mapping's key
     * @param v6  the sixth mapping's value
     * @param k7  the seventh mapping's key
     * @param v7  the seventh mapping's value
     * @param k8  the eighth mapping's key
     * @param v8  the eighth mapping's value
     * @param k9  the ninth mapping's key
     * @param v9  the ninth mapping's value
     * @param k10 the tenth mapping's key
     * @param v10 the tenth mapping's value
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @return a {@code Map} containing the specified mappings
     * @throws InternalError            if the length of input arguments is odd
     * @throws IllegalArgumentException if there are any duplicate keys
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return mapN(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }

    /**
     * 快速创建一个 Map
     * <p>
     * 用 {@link HashMap} 实现
     *
     * @param input input arguments
     * @param <K>   the key type
     * @param <V>   the value type
     * @return a {@code Map} containing the specified mappings
     * @throws InternalError            if the length of input arguments is odd
     * @throws IllegalArgumentException if there are any duplicate keys
     */
    @SuppressWarnings("unchecked")
    private static <K, V> Map<K, V> mapN(Object... input) {
        // 检查参数数量，必须是偶数个参数
        if ((input.length & 1) != 0) {
            throw new InternalError("length is odd");
        }

        Map<K, V> result = new HashMap<>();
        for (int i = 0; i < input.length; i += 2) {
            K k = (K) Objects.requireNonNull(input[i]);
            V v = (V) Objects.requireNonNull(input[i + 1]);
            if (result.containsKey(k)) {
                throw new IllegalArgumentException("duplicate key: " + k);
            } else {
                result.put(k, v);
            }
        }
        return result;
    }
}
