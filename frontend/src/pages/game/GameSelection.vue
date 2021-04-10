<template>
    <div class="bg-gray-900 color-white px-2">
        <h1 class="mt-10 mb-10 text-6xl text-white">
            Overview
        </h1>

        <div class="absolute top-20 right-8 mt-6">
            <button class="bg-green-600 hover:bg-green-900 text-white font-bold py-3 px-4 border order-gray-900 rounded">
                Spiel erstellen
            </button>
        </div>

        <div class="w-full mx-auto min-h-screen flex flex-col">
            <div class="px-6 py-8 rounded shadow-md bg-white text-black w-full">
                <table class="w-full text-center">
                    <thead>
                        <tr>
                            <th class="py-4 px-6 bg-grey-lightest font-bold uppercase text-sm text-grey-dark border-b border-gray-900">
                                Raum
                            </th>
                            <th class="py-4 px-6 bg-grey-lightest font-bold uppercase text-sm text-grey-dark border-b border-gray-900">
                                Spieleranzahl
                            </th>
                            <th class="py-4 px-6 bg-grey-lightest font-bold uppercase text-sm text-grey-dark border-b border-gray-900">
                                Themengebiet
                            </th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr class="hover:bg-gray-200 cursor-pointer">
                            <td class="py-4 px-6 border-b border-gray-900">
                                Raum XY
                            </td>
                            <td class="py-4 px-6 border-b border-gray-900">
                                13 Spieler
                            </td>
                            <td class="py-4 px-6 border-b border-gray-900">
                                Biologie
                            </td>
                        </tr>

                        <tr class="hover:bg-gray-200 cursor-pointer">
                            <td class="py-4 px-6 border-b border-gray-900">
                                Raum YX
                            </td>
                            <td class="py-4 px-6 border-b border-gray-900">
                                3 Spieler
                            </td>
                            <td class="py-4 px-6 border-b border-gray-900">
                                Filme
                            </td>
                        </tr>

                        <tr class="hover:bg-gray-200 cursor-pointer">
                            <td class="py-4 px-6 border-gray-900">
                                Raum Z
                            </td>
                            <td class="py-4 px-6 border-gray-900">
                                6 Spieler
                            </td>
                            <td class="py-4 px-6 border-gray-900">
                                Wissenschaft
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>
