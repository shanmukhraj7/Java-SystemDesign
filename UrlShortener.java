import java.util.*;


class Url{
    long id;
    String shortCode;
    String longUrl;
    long createdAt;

    public Url(long id, String shortCode, String longUrl){
        this.id = id;
        this.shortCode = shortCode;
        this.longUrl = longUrl;
        this.createdAt = System.currentTimeMillis();
    }
}

class Base62Encoder{
    public static final String Base62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String encode(long num){
        StringBuilder sb = new StringBuilder();
        while(num > 0){
            long rem = num % 62;
            sb.append(Base62.charAt((int) rem));
            num /= 62;
        }
        return sb.reverse().toString();
    }
}

class UrlRepository{
    private Map<String, Url> shortToUrl;
    private Map<String, Url> longToUrl;

    public UrlRepository(){
        this.shortToUrl = new HashMap<>();
        this.longToUrl = new HashMap<>();
    }

    public void save(Url url){
        shortToUrl.put(url.shortCode, url);
        longToUrl.put(url.longUrl, url);
    }

    public Url findByShortCode(String code){
        return shortToUrl.get(code);
    }

    public Url findByLongUrl(String longUrl){
        return longToUrl.get(longUrl);
    }
}

class UrlService{
    private UrlRepository repo;
    private Base62Encoder encoder;
    private long idCounter = 1;

    public UrlService(){
        this.repo = new UrlRepository();
        this.encoder = new Base62Encoder();
    }

    public String shortenUrl(String longUrl){
        Url existing = repo.findByLongUrl(longUrl);
        if(existing != null){
            return "short.ly/" + existing.shortCode;
        }
        long id = idCounter++;
        String shortCode = encoder.encode(id);
        Url url = new Url(id, shortCode, longUrl);
        repo.save(url);
        return "short.ly/" + shortCode;
    }

    public String resolveUrl(String shortCode){
        Url url = repo.findByShortCode(shortCode);
        if(url == null) return null;
        return url.longUrl;
    }
}


public class UrlShortener {
    public static void main(String[] args) {
        UrlService service = new UrlService();

        String short1 = service.shortenUrl("https://google.com/very/long/url");
        System.out.println("Short URL: " + short1);

        String short2 = service.shortenUrl("https://openai.com/chatgpt");
        System.out.println("Short URL: " + short2);

        String original = service.resolveUrl(short1.split("/")[1]);
        System.out.println("Resolved URL: " + original);
    }
}
