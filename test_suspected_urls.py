import urllib.request
import ssl

ssl_context = ssl._create_unverified_context()

candidates = {
    "Güvenli Bağlanma": [
        "https://cdn.timas.com.tr/urun/guvenli-baglanma-9786050815061.jpg",
        "https://cdn.timas.com.tr/urun/guvenli-baglanma-9786050820423.jpg"
    ],
    "Cezasız Eğitim 2": [
        "https://cdn.timas.com.tr/urun/cezasiz-egitim-2-9786050828757.jpg",
        "https://cdn.timas.com.tr/urun/cezasiz-egitim-2-9786050814675.jpg"
    ],
    "Bırak ve Rahatla": [
        "https://cdn.timas.com.tr/urun/birak-ve-rahatla-9786050830705.jpg",
        "https://cdn.timas.com.tr/urun/birak-ve-rahatla-9786050817027.jpg"
    ],
    "Çocukluk Sırrı": [
        "https://cdn.timas.com.tr/urun/cocukluk-sirri-9786050844979.jpg",
        "https://cdn.timas.com.tr/urun/cocukluk-sirri-9786050819762.jpg"
    ],
    "Çocuk Eğitiminde Yanlışlar": [
        "https://cdn.timas.com.tr/urun/cocuk-egitiminde-dogru-bilinen-yanlislar-9786050817584.jpg",
        "https://cdn.timas.com.tr/urun/cocuk-egitiminde-dogru-bilinen-yanlislar-9786050811122.jpg"
    ],
    "Mücella": [
        "https://cdn.timas.com.tr/urun/mucella-9786050820416.jpg",
        "https://cdn.timas.com.tr/urun/mucella-9786050819779.jpg"
    ],
    "Yusuf ile Züleyha": [
        "https://cdn.timas.com.tr/urun/yusuf-ile-zuleyha-9789753625579.jpg",
        "https://cdn.timas.com.tr/urun/yusuf-ile-zuleyha-9786050828351.jpg"
    ],
    "Mimoza Sürgünü": [
        "https://cdn.timas.com.tr/urun/mimoza-surgunu-9786050812350.jpg",
        "https://cdn.timas.com.tr/urun/mimoza-surgunu-9786050812732.jpg"
    ],
    "Lâ: Sonsuzluk Hecesi": [
        "https://cdn.timas.com.tr/urun/la-sonsuzluk-hecesi-9789752638518.jpg",
        "https://cdn.timas.com.tr/urun/la-sonsuzluk-hecesi-9786050828368.jpg",
        "https://cdn.timas.com.tr/urun/la-9789752638518.jpg"
    ],
    "Kehribar Geçidi": [
        "https://cdn.timas.com.tr/urun/kehribar-gecidi-9786050838091.jpg",
        "https://cdn.timas.com.tr/urun/kehribar-gecidi-9786050843002.jpg"
    ],
    "Göğü Yere İndirelim": [
        "https://cdn.timas.com.tr/urun/gogu-yere-indirelim-9786050823905.jpg",
        "https://cdn.timas.com.tr/urun/gogu-yere-indirelim-9786050824049.jpg"
    ],
    "Düşler Atlası": [
        "https://cdn.timas.com.tr/urun/dusler-atlasi-9786050829181.jpg",
        "https://cdn.timas.com.tr/urun/dusler-atlasi-9786050830026.jpg"
    ],
    "Huzur Sokağı": [
        "https://cdn.timas.com.tr/urun/huzur-sokagi-9789757544425.jpg",
        "https://cdn.timas.com.tr/urun/huzur-sokagi-ciltli-9786050830491.jpg",
        "https://cdn.timas.com.tr/urun/huzur-sokagi-9786050830491.jpg"
    ],
    "Son Ayı": [
        "https://cdn.timas.com.tr/urun/son-ayi-9786050843392.jpg",
        "https://cdn.timas.com.tr/urun/son-ayi-9786050843774.jpg",
        "https://cdn.timas.com.tr/urun/son-ayi-9786050844320.jpg"
    ]
}

results = {}
for book_title, urls in candidates.items():
    found = False
    for url in urls:
        try:
            req = urllib.request.Request(
                url, 
                headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'}
            )
            with urllib.request.urlopen(req, timeout=3, context=ssl_context) as response:
                if response.status == 200:
                    print(f"[OK] {book_title} -> {url}")
                    results[book_title] = url
                    found = True
                    break
        except Exception:
            pass
    if not found:
        print(f"[FAIL] {book_title}")

print("\nSummary:")
for title, url in results.items():
    print(f'"{title}": "{url}",')
