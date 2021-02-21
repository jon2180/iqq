package com.neutron.im.service;

import com.neutron.im.entity.Account;
import com.neutron.im.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService implements UserDetailsService {

    private final AccountMapper accountMapper;

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 通过用户名从数据库获取用户信息
        Account user = accountMapper.findOneByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

//        HttpSession session = request.getSession();
//        session.setAttribute(session.getId(), user);

        // 得到用户角色
//        String role = user.getRole();

        // 角色集合
        user.addAuthority(new SimpleGrantedAuthority("ROLE_" + "user"));
        return user;
    }

    @Autowired
    public AccountService(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    public boolean loginCheck(String email, String password) {
        var account = accountMapper.findOneByEmail(email);
        if (account == null) {
            return false;
        }

        return true;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public boolean registerCheck(String email, String password, String nickname) throws RuntimeException {
        throw new RuntimeException("No Implementation");
    }

    public List<Account> findAll() {
        return accountMapper.findAll();
    }

    public Account findOneByEmail(String email) {
        return accountMapper.findOneByEmail(email);
    }
}
