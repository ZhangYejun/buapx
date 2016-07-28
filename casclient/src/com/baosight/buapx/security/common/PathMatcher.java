package com.baosight.buapx.security.common;

/**
 * Strategy interface for String-based path matching. Used by
 * PathMatchingResourcePatternResolver, AbstractUrlHandlerMapping,
 * PropertiesMethodNameResolver, WebContentInterceptor.
 * 
 * <p>
 * The default implementation is AntPathMatcher, supporting Ant-style pattern
 * syntax.
 * 
 * @author Juergen Hoeller
 * @since 1.2
 * @see AntPathMatcher
 * @see org.springframework.core.io.support.PathMatchingResourcePatternResolver
 * @see org.springframework.web.servlet.handler.AbstractUrlHandlerMapping
 * @see org.springframework.web.servlet.mvc.multiaction.PropertiesMethodNameResolver
 * @see org.springframework.web.servlet.mvc.WebContentInterceptor
 */
public interface PathMatcher {

	/**
	 * Return if the given string represents a pattern to be matched via this
	 * class: If not, the "match" method does not have to be used because direct
	 * equality comparisons are sufficient.
	 * 
	 * @param str
	 *            the string to check
	 * @return whether the given string represents a pattern
	 * @see #match
	 */
	boolean isPattern(String str);

	/**
	 * Match a string against the given pattern.
	 * 
	 * @param pattern
	 *            the pattern to match against
	 * @param str
	 *            the string to test
	 * @return whether the arguments matched
	 */
	boolean match(String pattern, String str);

}