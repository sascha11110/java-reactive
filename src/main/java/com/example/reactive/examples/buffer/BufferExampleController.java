package com.example.reactive.examples.buffer;

import com.example.reactive.common.model.Comparison;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/example/buffer")
public class BufferExampleController {

    private final BufferExampleRepository repository;

    @Autowired
    BufferExampleController(final BufferExampleRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @ResponseBody
    public Flux<Comparison> test(@PathVariable final Long ssoUserId) {
        return repository.getComparisonsBySsoUserId(ssoUserId);
    }
}
