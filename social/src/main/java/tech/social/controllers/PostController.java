package tech.social.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tech.social.dtos.PostRequestDTO;
import tech.social.dtos.PostResponseDTO;
import tech.social.entities.Post;
import tech.social.entities.User;
import tech.social.repositories.PostRepository;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(
            @RequestBody @Valid PostRequestDTO postDTO,
            @AuthenticationPrincipal User currentUser) {
        Post post = new Post(postDTO.content(), currentUser);
        Post savedPost = postRepository.save(post);
        return ResponseEntity.ok(new PostResponseDTO(savedPost));
    }
    
    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> getAllPosts(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDTO> posts = postRepository.findAll(pageable)
                .map(PostResponseDTO::new);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/my-posts")
    public ResponseEntity<Page<PostResponseDTO>> getMyPosts(
            @AuthenticationPrincipal User currentUser,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDTO> posts = postRepository.findByAuthor(currentUser, pageable)
                .map(PostResponseDTO::new);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/user/{username}")
    public ResponseEntity<Page<PostResponseDTO>> getPostsByUsername(
            @PathVariable String username,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDTO> posts = postRepository.findByAuthor_Username(username, pageable)
                .map(PostResponseDTO::new);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return ResponseEntity.ok(new PostResponseDTO(post));
    }
    
    @PutMapping("/{id}") 
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable Long id,
            @RequestBody @Valid PostRequestDTO postDTO,
            @AuthenticationPrincipal User currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        // Verificar se o usuário logado é o autor do post
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build(); // Forbidden
        }
        post.setContent(postDTO.content());
        Post updatedPost = postRepository.save(post);
        return ResponseEntity.ok(new PostResponseDTO(updatedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        // Verificar se o usuário logado é o autor do post ou é admin
        boolean isAuthor = post.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAuthor && !isAdmin) {
            return ResponseEntity.status(403).build(); // Forbidden
        }
        postRepository.delete(post);
        return ResponseEntity.noContent().build();
    }
}