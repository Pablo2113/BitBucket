package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.GitMinerModelExports.GitMinerUser;
import aiss.bitbucketminer.model.USER_POJO.Users;

public class UserTransformerService {

    public static GitMinerUser transform(Users userdata) {

        GitMinerUser user= new GitMinerUser();
            user.setId(userdata.getAccountId());
            user.setUsername(userdata.getNickname());
            user.setName(userdata.getDisplayName());
            user.setAvatar_url(userdata.getLinks().getAvatar().getHref());
            user.setWeb_url(userdata.getLinks().getSelf().getHref());
            user.setAccountStatus(userdata.getAccountStatus());
            user.setNickname(userdata.getNickname());

            return user;
}
}
