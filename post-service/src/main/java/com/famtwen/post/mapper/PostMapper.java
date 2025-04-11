package com.famtwen.post.mapper;

import com.famtwen.post.dto.response.PostResponse;
import com.famtwen.post.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}
