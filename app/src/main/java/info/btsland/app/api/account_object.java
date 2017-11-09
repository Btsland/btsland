package info.btsland.app.api;


import java.util.List;

public class account_object {
    public String id;
    public String membership_expiration_date;
    public String registrar;
    public String referrer;
    public String lifetime_referrer;
    public int network_fee_percentage;
    public int lifetime_referrer_fee_percentage;
    public int referrer_rewards_percentage;
    public String name;
    public authority owner;
    public authority active;
    public types.account_options options;
    public String statistics;
    public List<String> whitelisting_accounts;
    public List<String> whitelisted_accounts;
    public List<String> blacklisted_accounts;
    public List<String> blacklisting_accounts;
    public List<Object> owner_special_authority;
    public List<Object> active_special_authority;
    public Integer top_n_control_flags;

    @Override
    public String toString() {
        return "account_object{" +
                "id='" + id + '\'' +
                ", membership_expiration_date='" + membership_expiration_date + '\'' +
                ", registrar='" + registrar + '\'' +
                ", referrer='" + referrer + '\'' +
                ", lifetime_referrer='" + lifetime_referrer + '\'' +
                ", network_fee_percentage=" + network_fee_percentage +
                ", lifetime_referrer_fee_percentage=" + lifetime_referrer_fee_percentage +
                ", referrer_rewards_percentage=" + referrer_rewards_percentage +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                ", active=" + active +
                ", options=" + options +
                ", statistics='" + statistics + '\'' +
                ", whitelisting_accounts=" + whitelisting_accounts +
                ", whitelisted_accounts=" + whitelisted_accounts +
                ", blacklisted_accounts=" + blacklisted_accounts +
                ", blacklisting_accounts=" + blacklisting_accounts +
                ", owner_special_authority=" + owner_special_authority +
                ", active_special_authority=" + active_special_authority +
                ", top_n_control_flags=" + top_n_control_flags +
                '}';
    }
}
