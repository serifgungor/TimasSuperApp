import urllib.request
import urllib.parse
import ssl

# Create unverified SSL context just in case
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

def check_url(url):
    try:
        req = urllib.request.Request(
            url, 
            headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'}
        )
        with urllib.request.urlopen(req, timeout=5, context=ssl_context) as response:
            return response.status == 200
    except Exception as e:
        return False

print("Checking book cover URLs...")
broken_books = []
for idx, book in enumerate(books):
    title = book["title"]
    cover = book["cover"]
    ok = check_url(cover)
    print(f"{idx+1:02d}. [{'OK' if ok else 'BROKEN'}] {title}: {cover}")
    if not ok:
        broken_books.append(book)

print(f"\nTotal broken books: {len(broken_books)}")
