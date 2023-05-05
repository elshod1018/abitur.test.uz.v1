package uz.test.abitur.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.test.abitur.config.security.UserSession;
import uz.test.abitur.domains.News;
import uz.test.abitur.dtos.news.NewsCreateDTO;
import uz.test.abitur.dtos.news.NewsDeleteDTO;
import uz.test.abitur.dtos.news.NewsUpdateDTO;
import uz.test.abitur.repositories.NewsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final UserSession userSession;



    public News create(NewsCreateDTO dto) {
        return newsRepository.save(News.builder()
                .createdBy(userSession.getId())
                .title(dto.getTitle())
                .body(dto.getBody())
                .build());
    }

   /* @Cacheable(value = "newsCache", key = "#id")*/
    public News getById(Integer id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
    }

    public List<News> getAll() {
        return newsRepository.getAll();
    }

    /*@CachePut(value = "newsCache", key = "#dto.id")*/
    public News update(NewsUpdateDTO dto) {
        News news = getById(dto.getId());
        news.setTitle(dto.getTitle());
        news.setBody(dto.getBody());
        news.setUpdatedAt(LocalDateTime.now());
        return newsRepository.save(news);
    }

    /*@CacheEvict(value = "newsCache", key = "#dto.id")*/
    public void delete(NewsDeleteDTO dto) {
        News news = getById(dto.getId());
        news.setDeleted(true);
        newsRepository.save(news);
    }
}
