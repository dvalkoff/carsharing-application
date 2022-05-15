function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showCars);
    } else {
        x.innerHTML = "Geolocation is not supported by this browser.";
    }
}

function showCars(position) {
    ymaps.ready(init)

    function init() {
        var myMap = new ymaps.Map('map', {
                center: [position.coords.latitude, position.coords.longitude],
                zoom: 16
            }, {
                searchControlProvider: 'yandex#search'
            }),
            objectManager = new ymaps.ObjectManager({
                // Чтобы метки начали кластеризоваться, выставляем опцию.
                clusterize: true,
                // ObjectManager принимает те же опции, что и кластеризатор.
                gridSize: 32,
                clusterDisableClickZoom: true
            });
        // Чтобы задать опции одиночным объектам и кластерам,
        // обратимся к дочерним коллекциям ObjectManager.
        objectManager.objects.options.set('preset', 'islands#blueAutoCircleIcon');
        objectManager.clusters.options.set('preset', 'islands#greenClusterIcons');
        myMap.geoObjects.add(objectManager);

        myPlacemark = new ymaps.Placemark(myMap.getCenter(), {
            hintContent: 'Я',
            balloonContent: 'Я'
        }, {
            // Опции.
            // Необходимо указать данный тип макета.
            iconLayout: 'default#image',
            // Своё изображение иконки метки.
            iconImageHref: 'images/squat-marker-red.svg',
            // Размеры метки.
            iconImageSize: [30, 42],
            // Смещение левого верхнего угла иконки относительно
            // её "ножки" (точки привязки).
            iconImageOffset: [-5, -38]
        })
        myMap.geoObjects.add(myPlacemark)

        $.ajax({
            url: "http://localhost:8080/ui/map?latitude=" + position.coords.latitude + "&longitude=" + position.coords.longitude
        }).done(function (data) {
            objectManager.add(data);
        });

    }
}

window.onload = function () {
    getLocation()
};