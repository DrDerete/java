Практическая работа 2
MAC – Message Authentication Code (имитовставка)
Цель практической работы состоит в разработке Java-программы получения
МАС-кода для строки текста. МАС представляет собой усовершенствованный
хеш-код, который использует ключ.
1. Использование класса МАС
В программе используйте класс МАС. Для создания экземпляра класса
МАС используйте метод   
mac.getInstance(String algorithm).
Класс SecureRandom пакета java.Security предоставляет надежный
генератор случайных чисел:
Доступные алгоритмы создания МАС-кодов: HmacSHA1, HmacSHA256,
HmacSHA384, HmacSHA512.
Класс KeyGenerator предоставляет метод с именем init(). Этот метод
принимает объект SecureRandom и инициализирует текущий генератор
ключей.
mac.init(key);
Процесс вычисления Мас. Обновление Мас массивом байтов:
void update(byte[] input);
Метод doFinal() класса Mac используется для завершения операции Mac.
Передайте необходимые данные в виде массива байтов этому методу.
2. Класс KeyGenerator для генерации ключа
Создание экземпляра класса KeyGenerator, предназначенного для
генерации ключа:
KeyGenerator kg=KeyGenerator.getInstance(algorithm);
Доступные алгоритмы создания МАС-кодов: HmacSHA1, HmacSHA256,
HmacSHA384, HmacSHA512.
Генерация секретного ключа
SecretKey key=kg.generateKey();
3. Хранилища ключей
   Хранилище ключей в Java — это безопасный механизм хранения,
   используемый для управления криптографическими ключами и
   сертификатами. Это набор записей ключей, каждая из которых
   идентифицируется псевдонимом, и может хранить закрытые ключи, открытые
   ключи, секретные ключи и доверенные сертификаты.
   Java предоставляет различные типы хранилищ ключей, каждое из которых
   имеет свой определенный формат и вариант использования:
   • JKS (Java Key Store): тип хранилища ключей по умолчанию,
   используемый Java. Он в основном используется для хранения пар ключей
   (закрытых и открытых ключей) и сертификатов
   • PKCS12: более широко используемый формат для хранения ключей и
   сертификатов. Это стандартный формат, поддерживаемый многими
   приложениями и инструментами, что делает его более совместимым
   • JCEKS (Java Cryptography Extension Keystore): используется для хранения
   секретных ключей, требующих более надежного шифрования, чем JKS
   • BKS (Bouncy Castle Keystore): формат хранилища ключей,
   предоставляемый поставщиком криптографии Bouncy Castle
4.  Класс KeyStore  
    Класс KeyStore в Java является частью пакета java.security и предоставляет
    комплексный API для управления криптографическими ключами и
    сертификатами. Он служит основным классом для взаимодействия с
    хранилищами ключей, позволяя нам безопасно создавать, загружать, хранить
    и извлекать записи.
    Вот некоторые из наиболее важных методов, предоставляемых классом
    KeyStore:
    getInstance(String type): Создает новое хранилище ключей.  
    Создаем экземпляр KeyStore указанного типа (e.g., JKS, PKCS12, JCEKS)  или по
    умолчанию KeyStore.getDefaultType().
    store(OutputStream stream, char[] password):
    подключение хранилища ключей к специальному потоку записи,
    защищенному паролем password.
    load(InputStream stream, char[] password): подключение
    хранилища ключей к специальному потоку чтения, защищенному паролем
    password.
    setEntry(String alias, KeyStore.Entry entry,
    KeyStore.ProtectionParameter protParam): добавляет (или
    обновляет) вход в хранилище ключей специальными псевдонимами.
    getEntry(String alias, KeyStore.ProtectionParameter
    protParam):  
    FileOutputStream fos = new FileOutputStream(
    "newKeyStoreName")){keystore.store(fos, password);}
    Извлекает запись из хранилища ключей, используя указанный псевдоним и
    параметры защиты.
5. Сохранение симметричного ключа
   Чтобы сохранить симметричный ключ, нам понадобятся три параметрв:
   • an alias— это просто имя, которое мы будем использовать в будущем для
   ссылки на запись
   • a key — который упакован в KeyStore.SecretKeyEntry
   • a password  — который упакован в то, что называется ProtectionParam
   KeyStore.SecretKeyEntry
   skEntry=
   SecretKeyEntry(mySecretKey);
   new
   KeyStore.
   KeyStore.ProtectionParameter password = new KeyStore.
   PasswordProtection(pwdArray);
   Помните, что пароль не может быть нулевым; однако он может быть пустой
   строкой. Если мы оставим пароль null для записи, мы получим
   KeyStoreException.
   java.security.KeyStoreException: non-null password required to
   create SecretKeyEntry.
   Нужно обернуть ключ и пароль в классы-обертки.
6. Задание.
   Напишите программу вычисления МАС-кода с использованием разных
   алгоритмов, доступных в Вашем JDK. Сделайте вывод о свойствах разных
   алгоритмов.
   Используйте относительно короткие и относительно длинные строки,
   например:
   «Machine learning, deep learning, and AI come up in
   countless articles, often outside of technology-minded
   publications. We’re promised a future of intelligent
   chatbots, self-driving  cars, and virtual assistants—a
   future sometimes painted in a grim light and other
   times as utopian, where human jobs will be scarce and
   most economic activity will be handled by robots or AI
   agents.»
   Или
   «For a future or current practitioner of machine
   learning, it’s important to be able to recognize the
   signal in the noise so that you can tell world-changing
   developments from overhyped press releases. Our future
   is at stake, and it’s a future in which you have an active
   role to play: after reading this book, you’ll be one of
   those who develop the AI agents.»
   Проверьте способность алгоритма создавать МАС-код:  - для одинаковых строк, - для разных строк, - для строк с перестановкой слов, - для строк с перестановкой букв. - используйте StringBuffer для вывода mac-кода текста в
   шестнадцатеричном формате. - используйте getAlgorithm() для вывода информации об алгоритме. - используйте getMacLength() для вывода информации о длите Мас
   кода.
7. Отчет
   Напишите отчет, содержащий текст программы с комментариями,
   результаты вычисления МАС-кодов с использованием разных алгоритмов,
   результаты проверки, выводы. 