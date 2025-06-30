package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.COMMENT_POJO.Value;
import aiss.bitbucketminer.model.GitMinerModelExports.GitMinerComment;
import aiss.bitbucketminer.model.GitMinerModelExports.GitMinerUser;

public class CommentsTransformService {



    public static GitMinerComment transform(Value value, Integer issuesId, GitMinerUser user) {
        if (value == null || value.getIssue() == null || !value.getIssue().getId().equals(issuesId)) {
            return null;
        }

        GitMinerComment comment = new GitMinerComment();
        comment.setId(value.getId().toString());
        comment.setBody(value.getContent().getRaw()==null?"Este comentario está vacío":value.getContent().getRaw());
        comment.setCreated_at(String.valueOf(value.getCreatedOn()));
        comment.setUpdated_at(value.getUpdatedOn()==null?"No se ha editado":String.valueOf(value.getUpdatedOn()));
        comment.setIssueId(value.getIssue().getId().toString());
        comment.setAuthor(user);

        return comment;
    }


}
