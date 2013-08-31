/*
 * Copyright (C) 2013 Zodiac Innovation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.zodiac.soa.client;

import java.io.IOException;
 
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Brian Estrada <brianseg014@gmail.com>
 */
@WebFilter(filterName = "ZodiacFilter", urlPatterns =  {"/*"})
public class ZodiacFilter implements Filter {
    
    @Override
    public void destroy() {
                 
    }
 
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        //if the ServletRequest is an instance of HttpServletRequest
        if(servletRequest instanceof HttpServletRequest) {
            //cast the object
            HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
            //create the FakeHeadersRequest object to wrap the HttpServletRequest
            HttpRequestHeadersWrapper request = new HttpRequestHeadersWrapper(httpServletRequest);
            //continue on in the filter chain with the FakeHeaderRequest and ServletResponse objects
            filterChain.doFilter(request, servletResponse);
        } else {
            //otherwise, continue on in the chain with the ServletRequest and ServletResponse objects
            filterChain.doFilter(servletRequest, servletResponse);
        }       
         
        return;
    }
 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
         
    }
    
}
