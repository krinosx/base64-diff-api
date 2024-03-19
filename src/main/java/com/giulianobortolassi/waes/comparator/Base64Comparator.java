package com.giulianobortolassi.waes.comparator;


/**
 * A text comparator interface. It was choose to create an abstraction layer for the comparator to enable the
 * replacement for the default implementation {@link DefaultBase64Comparator} with a powerful one if needed. A good
 * example of a powerful comparator implementation can be found in
 * <a href="https://github.com/google/diff-match-patch">https://github.com/google/diff-match-patch</a>. The Java
 * implementation is a single class and have a good performance and features.
 */
public interface Base64Comparator {
    /**
     * Given two texts, compare the size and content of both. If size are different the content will not be diff-ed.
     * If the size matches, the content is diff-ed.
     *
     * @param left Left side data to be compared
     * @param right Right side data to be compared
     * @return {@link ComparisionResult} with evaluation results.
     */
    ComparisionResult compare(String left, String right);
}

