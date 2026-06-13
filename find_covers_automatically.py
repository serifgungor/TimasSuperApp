import urllib.request
import urllib.parse
import ssl
import json
import re

ssl_context = ssl._create_unverified_context()

books = [
    {"title": "Malamander", "cover": "https://cdn.timas.com.tr/urun/malamander-9786050831634.jpg"},
    {"title": "Gargantis", "cover": "https://cdn.timas.com.tr/urun/gargantis-9786050834116.jpg"},
    {"title": "Shadowghast", "cover": "https://cdn.timas.com.tr/urun/shadowghast-9786050843231.jpg"},
    {"title": "Festergrimm", "cover": "https://cdn.timas.com.tr/urun/festergrimm-9786050846669.jpg"},
    {"title": "Mermedusa", "cover": "https://cdn.timas.com.tr/urun/mermedusa-9786050848984.jpg"},
    {"title": "Güvenli Bağlanma", "cover": "https://cdn.timas.com.tr/urun/guvenli-baglanma-9786050820423.jpg"},
    {"title": "Cezasız Eğitim 2", "cover": "https://cdn.timas.com.tr/urun/cezasiz-egitim-9786050814675.jpg"},
    {"title": "Bırak ve Rahatla", "cover": "https://cdn.timas.com.tr/urun/birak-ve-rahatla-9786050817027.jpg"},
    {"title": "Çocukluk Sırrı", "cover": "https://cdn.timas.com.tr/urun/cocukluk-sirri-9786050819762.jpg"},
    {"title": "Çocuk Eğitiminde Yanlışlar", "cover": "https://cdn.timas.com.tr/urun/cocuk-egitiminde-dogru-bilinen-yanlislar-9786050811122.jpg"},
    {"title": "Nar Ağacı", "cover": "https://cdn.timas.com.tr/urun/nar-agaci-9786050807073.jpg"},
    {"title": "Mücella", "cover": "https://cdn.timas.com.tr/urun/mucella-9786050819779.jpg"},
    {"title": "Yusuf ile Züleyha", "cover": "https://cdn.timas.com.tr/urun/yusuf-ile-zuleyha-9786050828351.jpg"},
    {"title": "Mimoza Sürgünü", "cover": "https://cdn.timas.com.tr/urun/mimoza-surgunu-9786050812732.jpg"},
    {"title": "Lâ: Sonsuzluk Hecesi", "cover": "https://cdn.timas.com.tr/urun/la-sonsuzluk-hecesi-9786050828368.jpg"},
    {"title": "Kehribar Geçidi", "cover": "https://cdn.timas.com.tr/urun/kehribar-gecidi-9786050843002.jpg"},
    {"title": "Mutluluğun İnşası", "cover": "https://cdn.timas.com.tr/urun/mutlulugun-insasi-9786050849745.jpg"},
    {"title": "Dilin Afetleri", "cover": "https://cdn.timas.com.tr/urun/dilin-afetleri-9786259445182.jpg"},
    {"title": "Kur'an Atlası", "cover": "https://cdn.timas.com.tr/urun/kuran-atlasi-9786256360525.jpg"},
    {"title": "Kalpsizler", "cover": "https://cdn.timas.com.tr/urun/kalpsizler-9786050847642.jpg"},
    {"title": "Tavuk Bacaklı Ev Kaçıyor", "cover": "https://cdn.timas.com.tr/urun/tavuk-bacakli-ev-kaciyor-9786259232645.jpg"},
    {"title": "Ağaçların Fısıltısı", "cover": "https://cdn.timas.com.tr/urun/agaclarin-fisiltisi-9786258618112.jpg"},
    {"title": "Öz Saygı Dersleri", "cover": "https://cdn.timas.com.tr/urun/oz-saygi-dersleri-9786050849851.jpg"},
    {"title": "Dirilt Kalbini", "cover": "https://cdn.timas.com.tr/urun/dirilt-kalbini-9786050825992.jpg"},
    {"title": "İçimdeki Müzik", "cover": "https://cdn.timas.com.tr/urun/icimdeki-muzik-9786050821277.jpg"},
    {"title": "Kiraz Ağacı ile Aramızdaki Mesafe", "cover": "https://cdn.timas.com.tr/urun/kiraz-agaci-ile-aramizdaki-mesafe-9786050828238.jpg"},
    {"title": "Göğü Yere İndirelim", "cover": "https://cdn.timas.com.tr/urun/gogu-yere-indirelim-9786050824049.jpg"},
    {"title": "Düşler Atlası", "cover": "https://cdn.timas.com.tr/urun/dusler-atlasi-9786050830026.jpg"},
    {"title": "Huzur Sokağı", "cover": "https://cdn.timas.com.tr/urun/huzur-sokagi-ciltli-9786050830491.jpg"},
    {"title": "Son Ayı", "cover": "https://cdn.timas.com.tr/urun/son-ayi-9786050844320.jpg"}
]

# We already know which ones are broken
broken_indices = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 15, 24, 25, 26, 27, 28, 29]

def verify_image(url):
    try:
        req = urllib.request.Request(
            url, 
            headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'}
        )
        with urllib.request.urlopen(req, timeout=5, context=ssl_context) as response:
            return response.status == 200
    except Exception:
        return False

def search_products(query_title):
    query = urllib.parse.quote(query_title)
    url = f"https://www.timas.com.tr/kitaplar-ve-setler?q={query}"
    try:
        req = urllib.request.Request(
            url,
            headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'}
        )
        with urllib.request.urlopen(req, timeout=10, context=ssl_context) as response:
            html = response.read().decode('utf-8')
            m = re.search(r'<script id="__NEXT_DATA__" type="application/json">(.*?)</script>', html)
            if m:
                data = json.loads(m.group(1))
                props = data.get("props", {}).get("pageProps", {})
                
                # Recursive search for all products listed
                products = []
                def extract_products(x):
                    if isinstance(x, dict):
                        if "title" in x and "image" in x and isinstance(x["image"], dict) and "src" in x["image"]:
                            products.append(x)
                        for val in x.values():
                            extract_products(val)
                    elif isinstance(x, list):
                        for item in x:
                            extract_products(item)
                
                extract_products(props)
                return products
    except Exception as e:
        print(f"Error searching for '{query_title}': {e}")
    return []

def normalize(text):
    text = text.lower()
    text = text.replace('ı', 'i').replace('ö', 'o').replace('ü', 'u').replace('ş', 's').replace('ç', 'c').replace('ğ', 'g')
    text = re.sub(r'[^a-z0-9\s]', '', text)
    return text.strip()

results = {}
for idx in broken_indices:
    book = books[idx]
    title = book["title"]
    print(f"\n[{idx+1:02d}] Searching for cover: '{title}'...")
    
    # Try searching with full title first
    found_products = search_products(title)
    
    # If not found, try a simplified title (e.g. without trailing numbers or extra annotations)
    if not found_products:
        simplified = re.sub(r'\s+\d+$', '', title) # remove trailing numbers like "2" in Cezasız Eğitim 2
        simplified = simplified.replace(':', '').strip()
        if simplified != title:
            print(f"  Trying simplified query: '{simplified}'")
            found_products = search_products(simplified)
            
    # If still not found, try splitting and searching by first two words
    if not ...: # just fallback if no products
        pass
        
    best_url = None
    if found_products:
        # Find the best match
        normalized_target = normalize(title)
        
        # Sort products by title match similarity
        matched_products = []
        for p in found_products:
            p_title = p.get("title", "")
            p_norm = normalize(p_title)
            
            # Simple score: 100 if exact match, otherwise size of intersection
            score = 0
            if p_norm == normalized_target:
                score = 100
            elif normalized_target in p_norm or p_norm in normalized_target:
                score = 50 + len(p_norm)
            else:
                target_words = set(normalized_target.split())
                p_words = set(p_norm.split())
                score = len(target_words.intersection(p_words))
            
            matched_products.append((score, p))
            
        matched_products.sort(key=lambda x: x[0], reverse=True)
        
        for score, p in matched_products:
            p_title = p.get("title", "")
            img_src = p["image"]["src"]
            img_url = f"https://cdn.timas.com.tr/{img_src}" if not img_src.startswith("http") else img_src
            
            # Verify if this cover exists
            print(f"  Candidate: '{p_title}' with URL: {img_url} (score: {score})")
            if verify_image(img_url):
                print(f"  -> VALID COVER FOUND: {img_url}")
                best_url = img_url
                break
            else:
                print("  -> Candidate URL returned error, trying next candidate...")
                
    if best_url:
        results[title] = best_url
    else:
        print(f"  -> WARNING: Could not find valid cover for '{title}'")

print("\n=== SEARCH RESULTS SUMMARY ===")
for title, url in results.items():
    print(f'"{title}": "{url}",')
