CREATE TABLE users
(
    id            BIGSERIAL,
    username      VARCHAR(100),
    password_hash TEXT,
    role          VARCHAR,
    ----------------------------------------------------------------
    CONSTRAINT users_id_pk PRIMARY KEY (id),
    CONSTRAINT username_uq UNIQUE (username),
    CONSTRAINT username_nn CHECK ( username IS NOT NULL ),
    CONSTRAINT password_hash_nn CHECK ( password_hash IS NOT NULL ),
    CONSTRAINT role_nn CHECK ( role IS NOT NULL )
);

COMMENT ON TABLE users IS 'Таблица пользователей';
COMMENT ON COLUMN users.id IS 'Id пользователя';
COMMENT ON COLUMN users.username IS 'Имя пользователя';
COMMENT ON COLUMN users.password_hash IS 'Хешированный пароль пользователя';
COMMENT ON COLUMN users.role IS 'Роль пользователя';

CREATE TABLE apartments
(
    id          BIGSERIAL,
    title       VARCHAR(100),
    description TEXT,
    address     VARCHAR(100),
    price       INT,
    rooms       INT,
    area        DECIMAL(7, 2),
    floor       INT,
    status      VARCHAR DEFAULT 'AVAILABLE',
    ----------------------------------------------------
    CONSTRAINT apartments_id_pk PRIMARY KEY (id),
    CONSTRAINT title_nn CHECK ( title IS NOT NULL ),
    CONSTRAINT address_nn CHECK ( address IS NOT NULL ),
    CONSTRAINT price_nn CHECK ( price IS NOT NULL ),
    CONSTRAINT rooms_nn CHECK ( rooms IS NOT NULL ),
    CONSTRAINT area_nn CHECK ( area IS NOT NULL ),
    CONSTRAINT floor_nn CHECK ( floor IS NOT NULL ),
    CONSTRAINT status_nn CHECK ( status IS NOT NULL )
);

COMMENT ON TABLE apartments IS 'Таблица квартир';
COMMENT ON COLUMN apartments.id IS 'Id квартиры';
COMMENT ON COLUMN apartments.title IS 'Название квартиры';
COMMENT ON COLUMN apartments.description IS 'Описание квартиры';
COMMENT ON COLUMN apartments.address IS 'Адрес квартиры';
COMMENT ON COLUMN apartments.price IS 'Цена квартиры';
COMMENT ON COLUMN apartments.rooms IS 'Количество комнат в квартире';
COMMENT ON COLUMN apartments.area IS 'Площадь квартиры в кв.м';
COMMENT ON COLUMN apartments.floor IS 'Этаж, на котором находится квартира';
COMMENT ON COLUMN apartments.status IS 'Статус квартиры';

CREATE TABLE viewing_requests
(
    id                 BIGSERIAL,
    user_id            BIGINT,
    apartment_id       BIGINT,
    preferred_datetime TIMESTAMP,
    ---------------------------------------------------------------------------------
    CONSTRAINT viewing_requests_id_pk PRIMARY KEY (id),
    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT apartment_id_fk FOREIGN KEY (apartment_id) REFERENCES apartments (id) ON DELETE CASCADE,
    CONSTRAINT preferred_datetime_nn CHECK ( preferred_datetime IS NOT NULL )
);

COMMENT ON TABLE viewing_requests IS 'Таблица записей на просмотр квартиры';
COMMENT ON COLUMN viewing_requests.id IS 'Id записи';
COMMENT ON COLUMN viewing_requests.user_id IS 'Id пользователя, который записывается';
COMMENT ON COLUMN viewing_requests.apartment_id IS 'Id квартиры, которую будут смотреть';
COMMENT ON COLUMN viewing_requests.preferred_datetime IS 'Дата и время просмотра';

CREATE TABLE favorites
(
    id           BIGSERIAL,
    user_id      BIGINT,
    apartment_id BIGINT,
    --------------------------------------------------------------------------------
    CONSTRAINT favorites_id_pk PRIMARY KEY (id),
    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT apartment_id_fk FOREIGN KEY (apartment_id) REFERENCES apartments (id) ON DELETE CASCADE,
    CONSTRAINT favorites_unique UNIQUE (user_id, apartment_id)
);

COMMENT ON TABLE favorites IS 'Таблица избранных квартир';
COMMENT ON COLUMN favorites.id IS 'Id записи';
COMMENT ON COLUMN favorites.user_id IS 'Id пользователя, который добавил квартиру в избранное';
COMMENT ON COLUMN favorites.apartment_id IS 'Id квартиры, добавленной в избранное';

CREATE TABLE purchases
(
    id            BIGSERIAL,
    user_id       BIGINT,
    apartment_id  BIGINT,
    purchase_date TIMESTAMP,
    ---------------------------------------------------------------------------------
    CONSTRAINT purchases_id_pk PRIMARY KEY (id),
    CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT apartment_id_fk FOREIGN KEY (apartment_id) REFERENCES apartments (id) ON DELETE CASCADE,
    CONSTRAINT purchase_date_nn CHECK ( purchase_date IS NOT NULL ),
    CONSTRAINT purchases_unique UNIQUE (user_id, apartment_id)
);

COMMENT ON TABLE purchases IS 'Таблица покупок квартир';
COMMENT ON COLUMN purchases.id IS 'Id покупки';
COMMENT ON COLUMN purchases.user_id IS 'Id пользователя, который купил квартиру';
COMMENT ON COLUMN purchases.apartment_id IS 'Id квартиры, которую купили';
COMMENT ON COLUMN purchases.purchase_date IS 'Дата и время покупки';

CREATE TABLE callback_requests
(
    id           BIGSERIAL,
    name         VARCHAR(100),
    phone        varchar(20),
    status       VARCHAR DEFAULT 'NEW',
    requested_at TIMESTAMP,
    -------------------------------------------------------------
    CONSTRAINT callback_request_id PRIMARY KEY (id),
    CONSTRAINT name_nn CHECK ( name IS NOT NULL ),
    CONSTRAINT phone_nn CHECK ( phone IS NOT NULL ),
    CONSTRAINT status_nn CHECK (status IS NOT NULL ),
    CONSTRAINT requested_at_nn CHECK ( requested_at IS NOT NULL )
);

COMMENT ON TABLE callback_requests IS 'Таблица заявок на обратную связь';
COMMENT ON COLUMN callback_requests.id IS 'Id заявки';
COMMENT ON COLUMN callback_requests.name IS 'Имя того, кто оставил заявку';
COMMENT ON COLUMN callback_requests.phone IS 'Номер телефона того, кто оставил заявку';
COMMENT ON COLUMN callback_requests.status IS 'Статус заявки';
COMMENT ON COLUMN callback_requests.requested_at IS 'Дата и время, когда была составлена заявка'
