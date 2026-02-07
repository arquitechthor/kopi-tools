package com.arquitechthor.kopi.links;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;

    public List<Link> getAllLinks() {
        return linkRepository.findAllByOrderByCategoryAsc();
    }

    public Map<String, List<Link>> getLinksGroupedByCategory() {
        return linkRepository.findAllByOrderByCategoryAsc().stream()
                .collect(Collectors.groupingBy(Link::getCategory));
    }

    public Optional<Link> getLinkById(Long id) {
        return linkRepository.findById(id);
    }

    public Link saveLink(Link link) {
        return linkRepository.save(link);
    }

    public void deleteLink(Long id) {
        linkRepository.deleteById(id);
    }
}
