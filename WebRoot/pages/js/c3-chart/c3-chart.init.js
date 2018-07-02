
$(function () {
var chart = c3.generate({
    bindto: '#combine-chart',
    data: {
        columns: [
            ['中文', 30, 20, 50, 40],
            ['data2', 200, 130, 90, 240],
            ['data3', 300, 200, 160, 400]
        ],
        types: {
            中文: 'bar',
            data2: 'bar',
            data3: 'bar'
        },

        groups: [
            ['中文','data2','data3']
        ],

        colors: {
            中文: '#4f81bd',
            data2: '#c0504d',
            data3: '#9bbb59'
        }
    },

    // barWidth: 10,

    axis: {
        x: {
            type: 'categorized'
        }
    }
});

});
