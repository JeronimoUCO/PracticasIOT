package backend.team.backend.security;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import backend.team.backend.security.filter.CustomAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public WebSecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().anyRequest().permitAll();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));




        /* CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().anyRequest().permitAll();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));


    //     CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
 */
    }

    //     http.authorizeRequests().antMatchers(PUT, "/usuario/**").hasAnyAuthority("ROLE_ADMIN");
    //     http.authorizeRequests().antMatchers(DELETE, "/usuario/**").hasAnyAuthority("ROLE_ADMIN");

    //     http.authorizeRequests().antMatchers(POST, "/bodega/**").hasAnyAuthority("ROLE_ADMIN");

    //     http.authorizeRequests().antMatchers(GET, "/agendamiento/**").hasAnyAuthority("ROLE_ADMIN");
    //     http.authorizeRequests().antMatchers(PUT, "/agendamiento/**").hasAnyAuthority("ROLE_ADMIN");
    //     http.authorizeRequests().antMatchers(DELETE, "/agendamiento/**").hasAnyAuthority("ROLE_ADMIN");

    //     http.authorizeRequests().antMatchers(POST, "/agendamiento/**").hasAnyAuthority("ROLE_USER");



    //     http.authorizeRequests().anyRequest().authenticated();
    //     http.addFilter(customAuthenticationFilter);
    //     http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    // }


    // @Bean
    // @Override
    // public AuthenticationManager authenticationManagerBean() throws Exception{
    //     return super.authenticationManagerBean();
    // }

    }

    