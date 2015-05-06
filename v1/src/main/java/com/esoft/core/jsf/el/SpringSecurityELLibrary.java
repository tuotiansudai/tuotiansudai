package com.esoft.core.jsf.el;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Taglib to combine the Spring-Security Project with Facelets <br />
 *
 * This is the class responsible holding the logic for making the tags work. <br />
 * The specified <code>public static</code> methods are also defined in the spring-security.taglib.xml
 * to enable them for usage as expression-language element. <br />
 * <br />
 * e.g.<code><br />
 * &lt;ui:component rendered='#{sec:ifAllGranted(&quot;ROLE_USER&quot;)'> blablabal &lt;/ui:component&gt;
 *
 *
 * @author Dominik Dorn - http://www.dominikdorn.com/
 * @version %I%, %G%
 * @since 0.5
 */
public class SpringSecurityELLibrary {

        private static Set<String> parseAuthorities(String grantedRoles) {
                Set<String> parsedAuthorities = new TreeSet<String>();
                if (grantedRoles == null || "".equals(grantedRoles.trim())) {
                        return parsedAuthorities;
                }

                String[] parsedAuthoritiesArr;
                if(grantedRoles.contains(",")){
                        parsedAuthoritiesArr = grantedRoles.split(",");
                } else {
                         parsedAuthoritiesArr = new String[]{grantedRoles};
                }

                // adding authorities to set (could pssible be done better!)
                for (String auth : parsedAuthoritiesArr)
                        parsedAuthorities.add(auth.trim());
                return parsedAuthorities;
        }

        private static GrantedAuthority[] getUserAuthorities()
        {
                if(SecurityContextHolder.getContext() == null)
                {
                        System.out.println("security context is empty, this seems to be a bug/misconfiguration!");
                        return new GrantedAuthority[0];
                }
                Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
                if(currentUser == null)
                        return new GrantedAuthority[0];


                Collection<GrantedAuthority> authorities = currentUser.getAuthorities();
                if(authorities == null)
                        return new GrantedAuthority[0];

                return authorities.toArray(new GrantedAuthority[]{});
        }


        /**
         * Method that checks if the user holds <b>any</b> of the given roles.
         * Returns <code>true, when the first match is found, <code>false</code> if no match is found and
         * also <code>false</code> if no roles are given
         *
         * @param grantedRoles a comma seperated list of roles
         * @return true if any of the given roles are granted to the current user, false otherwise
         */
        public static boolean ifAnyGranted(final String grantedRoles) {
                Set<String> parsedAuthorities = parseAuthorities(grantedRoles);
                if (parsedAuthorities.isEmpty())
                        return false;

                GrantedAuthority[] authorities = getUserAuthorities();

                for (GrantedAuthority authority : authorities) {
                        if (parsedAuthorities.contains(authority.getAuthority()))
                                return true;
                }
                return false;
        }



        /**
         * Method that checks if the user holds <b>all</b> of the given roles.
         * Returns <code>true</code>, iff the user holds all roles, <code>false</code> if no roles are given or
         * the first non-matching role is found
         *
         * @param requiredRoles a comma seperated list of roles
         * @return true if all of the given roles are granted to the current user, false otherwise or if no
         * roles are specified at all.
         */
        public static boolean ifAllGranted(final String requiredRoles) {
                // parse required roles into list
                Set<String> requiredAuthorities = parseAuthorities(requiredRoles);
                if (requiredAuthorities.isEmpty())
                        return false;

                // get granted roles
                GrantedAuthority[] authoritiesArray = getUserAuthorities();

                Set<String> grantedAuthorities = new TreeSet<String>();
                for (GrantedAuthority authority : authoritiesArray) {
                    grantedAuthorities.add(authority.getAuthority());
                }


                // iterate over required roles,
                for(String requiredAuthority : requiredAuthorities)
                {
                        // check if required role is inside granted roles
                        // if not, return false
                        if(!grantedAuthorities.contains(requiredAuthority)) {
                                return false;
                        }
                }
                return true;
        }

        /**
         * Method that checks if <b>none</b> of the given roles is hold by the user.
         * Returns <code>true</code> if no roles are given, or none of the given roles match the users roles.
         * Returns <code>false</code> on the first matching role.
         *
         * @param notGrantedRoles a comma seperated list of roles
         * @return true if none of the given roles is granted to the current user, false otherwise
         */
        public static boolean ifNotGranted(final String notGrantedRoles) {
                Set<String> parsedAuthorities = parseAuthorities(notGrantedRoles);
                if (parsedAuthorities.isEmpty())
                        return true;

                GrantedAuthority[] authorities = getUserAuthorities();

                for (GrantedAuthority authority : authorities) {
                        if (parsedAuthorities.contains(authority.getAuthority()))
                                return false;
                }
                return true;
        }
        
  /**
   * Method checks if the user is authenticated.
   * Returns <code>true</code> if the user is <b>not</b> anonymous.
   * Returns <code>false</code> if the user <b>is</b> anonymous.
   * @return
   */
  public static boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return false;
    }
    return authentication.isAuthenticated();
  }

  /**
   * Method checks if the user is anonymous.
   * Returns <code>true</code> if the user <b>is</b> anonymous.
   * Returns <code>false</code> if the user is <b>not</b> anonymous.
   * @return
   */
  public static boolean isAnonymous() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return true;
    }
    return !authentication.isAuthenticated();
  }

        public SpringSecurityELLibrary() {
        }
}